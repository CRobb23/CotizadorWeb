package com.digitalgeko.servicebus.util;

import com.digitalgeko.servicebus.exceptions.CSVMarshalException;
import com.digitalgeko.servicebus.exceptions.XMLMarshalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class CSVMarshalUtil {

	private static final Logger log = LoggerFactory.getLogger(CSVMarshalUtil.class);

	public static String toCSV(Class clazz, Object object) throws CSVMarshalException {
		try {
			Field[] fields = sortCsvFields(clazz.getDeclaredFields());
			StringBuilder builder = new StringBuilder();
			for (Field f : fields) {
				f.setAccessible(true);
				builder.append(f.get(object)).append(",");
			}
			// remove last comma
			String fullStr = builder.toString();
			if (fullStr.length() > 0)
				fullStr = fullStr.substring(0, fullStr.length()-1);
			return fullStr;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CSVMarshalException(e.getMessage(), e);
		}
	}
	
	public static Object fromCSV(Class clazz, String csv) throws CSVMarshalException {
		try {
			Field[] fields = sortCsvFields(clazz.getDeclaredFields());
			Object object = clazz.newInstance();
			StringTokenizer tokenizer = new StringTokenizer(csv, ",");
			int counter = tokenizer.countTokens();
			for (int i=0; i<counter; i++) {
				String token = tokenizer.nextToken();
				Field field = fields[i];
				field.setAccessible(true);
				field.set(object, token);
			}
			return object;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new CSVMarshalException(e.getMessage(), e);
        }
	}

	private static Field[] sortCsvFields(Field[] fields) {
		Arrays.sort(fields, (o1, o2) -> {
			CsvOrder or1 = o1.getAnnotation(CsvOrder.class);
			CsvOrder or2 = o2.getAnnotation(CsvOrder.class);
			if (or1 != null && or2 != null) {
				return or1.value() - or2.value();
			} else if (or1 != null && or2 == null) {
				return -1;
			} else if (or1 == null && or2 != null) {
				return 1;
			}
			return o1.getName().compareTo(o2.getName());
		});
		return fields;
	}
}
