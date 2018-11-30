package helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.read.biff.BiffException;
import be.objectify.led.PropertyContext;
import be.objectify.led.PropertySetter;
import jxl.WorkbookSettings;

public class ExcelBinder {
	
	public static <T> List bind(File file, Class<T> modelType, int sheetNumber) throws BiffException, IOException, InstantiationException, IllegalAccessException {
	WorkbookSettings ws = new WorkbookSettings();
	ws.setEncoding("Cp1252");
        jxl.Workbook workbook = jxl.Workbook.getWorkbook(file, ws);
        jxl.Sheet sheet = workbook.getSheet(sheetNumber);
        List<String> headerNames = getHeaderNames(sheet);
        
        List<T> objects = new ArrayList<T>();
        // iterate over each non-header row and make it into an object
        for (int i = 1; i < sheet.getRows(); i++) {
            PropertyContext propertyContext = getPropertyContext(sheet,i,headerNames);
            PropertySetter propertySetter = new PropertySetter(propertyContext);
            T t = modelType.newInstance();
            propertySetter.process(t);
            objects.add(t);
        }
        
        return objects;
    }

    private static List<String> getHeaderNames(Sheet sheet) {
        List<String> headerNames = new ArrayList<String>();
        Cell[] headerCells = sheet.getRow(0);
        for (Cell cell : headerCells) {
        	if (cell.getContents()!=null && !cell.getContents().isEmpty()) {
        		headerNames.add(cell.getContents());
        	}
        }

        return headerNames;
    }

    private static PropertyContext getPropertyContext(Sheet sheet, int rowIndex, List<String> headerNames) {
        Cell[] cells = sheet.getRow(rowIndex);
        MapPropertyContext propertyContext = new MapPropertyContext();
        int i=0;
        for (Cell cell : cells) {
        	if (i< headerNames.size()) {
        		propertyContext.setValue(headerNames.get(i), cell.getContents());
        	}
        	i++;
        }
        
        return propertyContext;
    }
}
