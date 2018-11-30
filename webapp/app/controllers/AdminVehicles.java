package controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.ExcelBinder;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.*;
import models.dto.VehicleBrandDTO;
import objects.VehicleLine;
import objects.VehicleLoJack;
import objects.VehicleValue;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminVehicles extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	private static void filterBrands(String brand) {
		
		//Validate the parameters
		boolean validBrand = GeneralMethods.validateParameter(brand);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validBrand) {
			filter.addQuery("name like ?", "%"+brand+"%");
		}
		
		int page;
		if (params.get("page") == null) {
			// verifying if it was in other page
			if (session.get("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(session.get("page"));
			}
		} else {
			page = Integer.parseInt(params.get("page"));
			session.put("page", page);
		}
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		ModelPaginator brands = null;
		if (filter.isValidFilter()) {			
			brands = new ModelPaginator(ER_Vehicle_Brand.class, filter.getQuery(), filter.getParametersArray());
		} else {
			brands = new ModelPaginator(ER_Vehicle_Brand.class);
		}
		
		brands.orderBy("name ASC");
		brands.setPageNumber(page);
		brands.setPageSize(20);
		renderArgs.put("brands", brands);
	}
	
	private static void filterLines(Long brandId, String line) {
		
		//Validate the parameters
		boolean validBrand = GeneralMethods.validateParameter(brandId);
		boolean validLine = GeneralMethods.validateParameter(line);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validBrand) {
			filter.addQuery("brand.id = ?", brandId);
		}
		
		if (validLine) {
			filter.addQuery("name like ?", "%"+line+"%", Operator.AND);
		}
		
		int page;
		if (params.get("page") == null) {
			// verifying if it was in other page
			if (session.get("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(session.get("page"));
			}
		} else {
			page = Integer.parseInt(params.get("page"));
			session.put("page", page);
		}
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		ModelPaginator lines = null;
		if (filter.isValidFilter()) {			
			lines = new ModelPaginator(ER_Vehicle_Line.class, filter.getQuery(), filter.getParametersArray());
		} else {
			lines = new ModelPaginator(ER_Vehicle_Line.class);
		}
		
		lines.orderBy("brand.name, name ASC");
		lines.setPageNumber(page);
		lines.setPageSize(20);
		renderArgs.put("lines", lines);
	}
	
	private static void filterVehicleValues(Long brandId, Long lineId) {
		
		//Validate the parameters
		boolean validBrand = GeneralMethods.validateParameter(brandId);
		boolean validLine = GeneralMethods.validateParameter(lineId);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validBrand) {
			filter.addQuery("line.brand.id = ?", brandId);
		}
		
		if (validLine) {
			filter.addQuery("line.id = ?", lineId);
		}

		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		ModelPaginator vehicleValues = null;
		if (filter.isValidFilter()) {			
			vehicleValues = new ModelPaginator(ER_Vehicle_Value.class, filter.getQuery(), filter.getParametersArray());
		} else {
			vehicleValues = new ModelPaginator(ER_Vehicle_Value.class);
		}
		
		vehicleValues.orderBy("line.brand.name, line.name, year ASC");
		vehicleValues.setPageSize(20);
		renderArgs.put("vehicleValues", vehicleValues);
	}
	
	/*
	 * ************************************************************************************************************************
	 * Brands list
	 * ************************************************************************************************************************
	 */
	
    public static void vehicleBrands(String name, boolean intern) {
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add brands to the renderArgs
    	filterBrands(name);
    	
    	//Render the list
    	render();
    }
    
    public static void searchBrands(String brand, String all, boolean intern, boolean internalSearch) {
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		vehicleBrands(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",brand);
    	}
    	vehicleBrands(brand,true);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Brands creation and editing
	 * ************************************************************************************************************************
	 */
    
    public static void editVehicleBrand(Long id) {
    	if (id!=null) {
    		ER_Vehicle_Brand brand = ER_Vehicle_Brand.findById(id);
    		renderArgs.put("brand", brand);
    	}
    	
    	render();
    }
    
    public static void saveBrand(@Valid ER_Vehicle_Brand brand) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("brand.create.fielderrors"));
    		validation.keep();
    		editVehicleBrand(brand.id);
    	} else {
    		if (brand.id != null) {
    			brand.save();
    			flash.success(Messages.get("brand.edit.success"));
    		} else {
    			ER_Vehicle_Brand similarValue = ER_Vehicle_Brand.find("name = ?", brand.name).first();
    			
    			if (similarValue==null) {
    				brand.save();
        			flash.success(Messages.get("brand.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("brand.create.duplicate"));
    				editVehicleBrand(brand.id);
    			}
    		}
    	}
    	
    	String name = session.get("name");

		if (name != null) {    			
			searchBrands(name, "",true,true);
		} else {
			vehicleBrands(name, true);
		}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Lines list
	 * ************************************************************************************************************************
	 */
    
    public static void vehicleLines(Long brandId, String line, boolean intern) {
    	if (!intern) {
    		session.remove("page");
    		session.remove("brandId");
    		session.remove("line");
    	}
    	
    	if (brandId!=null) {
	    	ER_Vehicle_Brand brand = ER_Vehicle_Brand.findById(brandId);
	    	renderArgs.put("brand", brand);
    	}
    	
    	List <ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("order by name").fetch();
    	renderArgs.put("brands", brands);
    	
    	//Add lines to the renderArgs
    	filterLines(brandId, line);
    	
    	render();
    }
    
    public static void searchLines(Long brandId, String line, String all, boolean intern, boolean internalSearch) {
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("brandId");
			session.remove("line");
			
			vehicleLines(null, null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	if(!intern) {
    		session.put("brandId",brandId);
			session.put("line",line);
    	}
    	//Render the list
    	vehicleLines(brandId, line, true);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Lines creation and editing
	 * ************************************************************************************************************************
	 */
    
    public static void editVehicleLine(Long id, Long brandId) {
    	if (id!=null) {
    		ER_Vehicle_Line line = ER_Vehicle_Line.findById(id);
    		renderArgs.put("line", line);
    	}
    	
    	if (brandId!=null) {
    		ER_Vehicle_Brand brand = ER_Vehicle_Brand.findById(brandId);
    		renderArgs.put("brand", brand);
    	} else {
    		List <ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("order by name").fetch();
        	renderArgs.put("brands", brands);
    	}
    	
    	List <ER_Vehicle_Class> classes = ER_Vehicle_Class.find("order by code").fetch();
    	renderArgs.put("classes", classes);
    	
    	render();
    }
    
    public static void saveLine(@Valid ER_Vehicle_Line line, Long brandId) {
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		flash.error(Messages.get("line.create.fielderrors"));
    		params.flash();
    		validation.keep();
    		editVehicleLine(line.id, brandId);
    	} else {
    		if (line.id!=null) {
    			ER_Vehicle_Line currentLine = ER_Vehicle_Line.findById(line.id);
    			currentLine.name = line.name;
    			currentLine.transferCode = line.transferCode;
    			currentLine.brand = line.brand;
    			currentLine.insurable = line.insurable;
    			currentLine.loJackYear = line.loJackYear;
//    			currentLine.requiresLoJack = line.requiresLoJack;
    			currentLine.vehicleClass = line.vehicleClass;
    			currentLine.save();
    			flash.success(Messages.get("line.edit.success"));
    			
    		} else {
    			
    			ER_Vehicle_Line similarValue = ER_Vehicle_Line.find("name = ? and brand.id = ?", line.name, line.brand.id).first();
    			
    			if (similarValue==null) {
    				line.save();
        			flash.success(Messages.get("line.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("line.create.duplicate"));
    				editVehicleLine(line.id, brandId);
    			}
    		}
    		
    		if (brandId==null && line.brand!=null) {
    			brandId = line.brand.id;
    		}
    		
    		Long  sessionBrandId = "".equals(session.get("brandId")) || "null".equals(session.get("brandId")) ||session.get("brandId")==null ? null : Long.parseLong(session.get("brandId"));
    		String sessionLine = session.get("line");

    		if (sessionBrandId != null || sessionLine != null) {    			
    			searchLines(sessionBrandId,sessionLine, "",true,true);
    		} else {
    			vehicleLines(sessionBrandId,sessionLine, true);
    		}
    		
    		
    	}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Vehicles list
	 * ************************************************************************************************************************
	 */
    
    public static void vehicleValues(Long lineId, Long brandId) {
    	if (lineId!=null) {
	    	ER_Vehicle_Line line = ER_Vehicle_Line.findById(lineId);
	    	renderArgs.put("line", line);
    	}
    	
    	filterVehicleValues(brandId,lineId);
    	
        render();
    }
    
    /*
	 * ************************************************************************************************************************
	 * Vehicles creation and editing
	 * ************************************************************************************************************************
	 */
    
    public static void editVehicleValue(Long id, Long lineId, Long yearId) {
    	if (id!=null) {
    		ER_Vehicle_Value vehicleValue = ER_Vehicle_Value.findById(id);
    		renderArgs.put("vehicleValue", vehicleValue);
    		
    		if (vehicleValue.line!=null) {
	    		List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("brand.id = ? order by name", vehicleValue.line.brand.id).fetch();
	    		renderArgs.put("lines", lines);
    		}
    	}
    	
    	if (lineId!=null) {
    		ER_Vehicle_Line line = ER_Vehicle_Line.findById(lineId);
    		renderArgs.put("line", line);
    	} else {
    		List <ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("order by name").fetch();
        	renderArgs.put("brands", brands);
    	}
    	
    	if (flash.contains("brand.id") && id==null) {
    		String brandIdString = flash.get("brand.id");
    		if (brandIdString!=null && !brandIdString.isEmpty()) {
	    		Long brandId = Long.parseLong(brandIdString);
	    		List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("brand.id = ? order by name", brandId).fetch();
	    		renderArgs.put("lines", lines);
    		}
    	}
    	
    	if(yearId!=null) {
    		ER_Year selectedYear = ER_Year.findById(yearId);
    		renderArgs.put("yearValue", selectedYear.id);
    	}
    	
    	List<ER_Year> years = ER_Year.find("order by year").fetch();
    	renderArgs.put("years", years);
    	
    	render();
    }
    
    public static void saveVehicleValue(@Valid ER_Vehicle_Value vehicleValue, Long lineId) {
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		flash.error(Messages.get("vehicleValue.create.fielderrors"));
    		params.flash();
    		validation.keep();
    		Long yearId = null;
    		if(vehicleValue.year!=null) {
    			 yearId=vehicleValue.year.id;
    		}
    		editVehicleValue(vehicleValue.id, lineId,yearId);
    	} else {
    		if (vehicleValue.id!=null) {
    			ER_Vehicle_Value currentVehicleValue = ER_Vehicle_Value.findById(vehicleValue.id);
    			currentVehicleValue.line = vehicleValue.line;
    			currentVehicleValue.value = vehicleValue.value;
    			currentVehicleValue.year = vehicleValue.year;
    			currentVehicleValue.save();
    			flash.success(Messages.get("vehicleValue.edit.success"));
    			
    		} else {
    			
    			ER_Vehicle_Value similarValue = ER_Vehicle_Value.find("line.id = ? and year = ?", vehicleValue.line.id, vehicleValue.year).first();
    			
    			if (similarValue==null) {
	    			vehicleValue.save();
	    			flash.success(Messages.get("vehicleValue.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("vehicleValue.create.duplicate"));
    				editVehicleValue(vehicleValue.id, lineId,null);
    			}
    		}
    		
    		if (lineId==null && vehicleValue.line!=null) {
    			lineId = vehicleValue.line.id;
    		}
    		
    		vehicleValues(lineId, null);
    	}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Vehicles massive update
	 * ************************************************************************************************************************
	 */
    
    public static void vehicleValuesMassiveUpdate() {
    	render();
    }
    
    private static void processValuesFile(File valuesFile) {
    	
    	Integer processedCount = 0;
    	
    	try {			
			List<VehicleValue> values = ExcelBinder.bind(valuesFile, VehicleValue.class, 1);
			
			List<Map<String, Object>> errorValues = new ArrayList<Map<String, Object>>(); 
			
			int fileLine = 1;
			for (VehicleValue newValue : values) {
				fileLine++;
				
				if (!GeneralMethods.validateParameter(newValue.line) && !GeneralMethods.validateParameter(newValue.brand) && 
						!GeneralMethods.validateParameter(newValue.year) && !GeneralMethods.validateParameter(newValue.value)) {
					continue;
				}
				
				ER_Vehicle_Brand brand = ER_Vehicle_Brand.find("name = ?", newValue.brand).first();
				ER_Vehicle_Line line = ER_Vehicle_Line.find("name = ? and brand.name = ?", newValue.line, newValue.brand).first();
				ER_Vehicle_Value value = ER_Vehicle_Value.find("year = ? and line.name = ? and line.brand.name = ?", Integer.parseInt(newValue.year), newValue.line, newValue.brand).first();
				
				if (brand==null) {
					Map<String, Object> errorMap = new HashMap<String, Object>();
					errorMap.put("error", Messages.get("vehicleValue.massiveupdate.nobrand", newValue.brand));
					errorMap.put("fileLine", fileLine);
					errorMap.put("value", newValue);
					errorValues.add(errorMap);
					continue;
				}
				
				if (line==null) {
					Map<String, Object> errorMap = new HashMap<String, Object>();
					errorMap.put("error", Messages.get("vehicleValue.massiveupdate.noline", newValue.line));
					errorMap.put("fileLine", fileLine);
					errorMap.put("value", newValue);
					errorValues.add(errorMap);
					continue;
				}
				
				if (GeneralMethods.validateParameter(newValue.year) && GeneralMethods.validateParameter(newValue.value)) {
					if (value==null) {
						value = new ER_Vehicle_Value();
						value.line = line;
					}
					value.year = ER_Year.find("year = ?", newValue.year).first();
					value.value = new BigDecimal(newValue.value.trim());
					
					if (validation.valid(value).ok) {
						value.save();
						processedCount++;
					} else {
						Map<String, Object> errorMap = new HashMap<String, Object>();
						errorMap.put("error", Messages.get("vehicleValue.massiveupdate.allrequiredvalue"));
						errorMap.put("fileLine", fileLine);
						errorMap.put("value", newValue);
						errorValues.add(errorMap);
						
					}
				} else {
					Map<String, Object> errorMap = new HashMap<String, Object>();
					errorMap.put("error", Messages.get("vehicleValue.massiveupdate.allrequiredvalue"));
					errorMap.put("fileLine", fileLine);
					errorMap.put("value", newValue);
					errorValues.add(errorMap);					
				}
			}
			renderArgs.put("errorVehicleValues", errorValues);
			
		} catch (Exception e) {
			Logger.error(e, "Error processing car values file");
		}
    	
    	renderArgs.put("valuesProcessedCount", processedCount);
    }
    
    private static void processLinesFile(File linesFile) {
    	
    	Integer processedCount = 0;
    	
    	try {			
			List<VehicleLine> lines = ExcelBinder.bind(linesFile, VehicleLine.class, 0);
			
			List<Map<String, Object>> errorLines = new ArrayList<Map<String, Object>>(); 
			int fileLine = 1;
			
			List<ER_Vehicle_Class> classesList = ER_Vehicle_Class.findAll();
			Map<String, ER_Vehicle_Class> classesMap = new HashMap<String, ER_Vehicle_Class>();
			for (ER_Vehicle_Class vehicleClass :  classesList) {
				classesMap.put(vehicleClass.code, vehicleClass);
			}
			
			for (VehicleLine newLine : lines) {
				
				fileLine++;
				if (!GeneralMethods.validateParameter(newLine.line) && !GeneralMethods.validateParameter(newLine.brand) && 
						!GeneralMethods.validateParameter(newLine.insurable)) {
					continue;
				}
				
				ER_Vehicle_Brand brand = ER_Vehicle_Brand.find("name = ?", newLine.brand).first();
				ER_Vehicle_Line line = ER_Vehicle_Line.find("name = ? and brand.name = ?", newLine.line, newLine.brand).first();
				ER_Vehicle_Class vehicleClass = classesMap.get(newLine.vehicleClass);
				
				if (brand==null && GeneralMethods.validateParameter(newLine.brand)) {
					brand = new ER_Vehicle_Brand();
					brand.name = newLine.brand;
					brand.save();
				}
				
				if (GeneralMethods.validateParameter(newLine.line)) {
					if (line==null) {
						line = new ER_Vehicle_Line();
						line.name = newLine.line;
						line.brand = brand;
					}
					
					line.insurable = !GeneralMethods.validateParameter(newLine.insurable)?true:Integer.parseInt(newLine.insurable)>0?true:false;
					line.loJackYear = !GeneralMethods.validateParameter(newLine.loJackYear)?null:Integer.parseInt(newLine.loJackYear);
					line.loJackYear = (line.loJackYear!=null && line.loJackYear>0) ? line.loJackYear : null;
					line.vehicleClass = vehicleClass;
					
					if (validation.valid(line).ok) {
						line.save();
						processedCount++;
					} else {
						Map<String, Object> errorMap = new HashMap<String, Object>();
						errorMap.put("error", Messages.get("vehicleValue.massiveupdate.allrequiredline"));
						errorMap.put("fileLine", fileLine);
						errorMap.put("line", newLine);
						errorLines.add(errorMap);
					}
					
				} else {
					Map<String, Object> errorMap = new HashMap<String, Object>();
					errorMap.put("error", Messages.get("vehicleValue.massiveupdate.allrequiredline"));
					errorMap.put("fileLine", fileLine);
					errorMap.put("line", newLine);
					errorLines.add(errorMap);
				}
			}
			
			renderArgs.put("errorLines", errorLines);
		} catch (Exception e) {
			Logger.error(e, "Error processing lines file");
		}
    	
    	renderArgs.put("linesProcessedCount", processedCount);
    }
    
    public static void downloadBrands() {
    	List<ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("order by name ASC").fetch();

    	List<VehicleBrandDTO> brandList = new ArrayList<>();
    	for (ER_Vehicle_Brand brand : brands) {
    	    for (ER_Vehicle_Line line : brand.lines) {
    	        VehicleBrandDTO brandDTO = new VehicleBrandDTO(brand.name, line.name, (line.vehicleClass != null ? line.vehicleClass.code : null), line.insurableIntValue());
    	        brandList.add(brandDTO);
            }
        }
    	
    	request.format = "xls";
    	//renderTemplate("Reports/MarcasYLineas.xls", brands);
        renderTemplate("Reports/MarcasLineas.xls", brandList);
    }

    public static void downloadValues() {
        List<ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("order by name ASC").fetch();

        List<VehicleBrandDTO> brandList = new ArrayList<>();
        for (ER_Vehicle_Brand brand : brands) {
            for (ER_Vehicle_Line line : brand.lines) {
                for (ER_Vehicle_Value value : line.values) {
                    VehicleBrandDTO brandDTO = new VehicleBrandDTO(brand.name, line.name, (value.year != null ? value.year.year : ""), value.value);
                    brandList.add(brandDTO);
                }
            }
        }

        request.format = "xls";
        //renderTemplate("Reports/MarcasYLineas.xls", brands);
        renderTemplate("Reports/MarcasLineasValores.xls", brandList);
    }

    public static void uploadValues(File valuesFile) {
    	
    	if (valuesFile!=null) {
    		processLinesFile(valuesFile);
    		processValuesFile(valuesFile);
    		render();
    	}
    	
    	flash.error(Messages.get("vehicleValue.masiveupdate.error"));
    	vehicleValuesMassiveUpdate();
    }
    
     /*
     * *****************************************************************
     * Download CSV template for Brands
     * *****************************************************************
     */
    public static void downloadNewVehiculeBrandTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Marcas-Vehiculo.xls");
    	renderTemplate("Reports/VehiculeBrands.xls");
    	
    }
    
    public static void loadVehiculeBrandFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				if(!sheet.getCell(2,row).getContents().trim().equals("")) {
    				
    					ER_Vehicle_Brand oldBrand = null;
        				if(!sheet.getCell(1,row).getContents().trim().equals("")) {
        					oldBrand = ER_Vehicle_Brand.find("name = ? and transferCode = ?", sheet.getCell(2,row).getContents(),sheet.getCell(1,row).getContents()).first();
        				}else {
        					oldBrand = ER_Vehicle_Brand.find("name = ? ", sheet.getCell(2,row).getContents()).first();
        				}
        				
        				if (oldBrand==null) {
        				
        					ER_Vehicle_Brand newVehiculeBrand = new ER_Vehicle_Brand();
                    		newVehiculeBrand.name = sheet.getCell(2,row).getContents();
                    		if(!sheet.getCell(3,row).getContents().trim().equals("")) {
                    			newVehiculeBrand.theftDeductible = new BigDecimal(sheet.getCell(3,row).getContents());
                    		}
                    		
                    		newVehiculeBrand.save();
                    		objectsCreated++;
        				}
    				}
    			}catch(Exception e) {
    				flash.error(Messages.get("vehiculeBrand.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("vehiculeBrand.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("vehiculeBrand.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("vehiculeBrand.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newVehiculeBrandFromExcel();
    	
    }
    
    public static void newVehiculeBrandFromExcel() {
    	render();
    }
    
    /*
     * *****************************************************************
     * Download CSV template for Lines
     * *****************************************************************
     */
    public static void downloadNewVehiculeLineTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Lineas-Vehiculo.xls");
    	renderTemplate("Reports/VehiculeLines.xls");
    	
    }
    
    public static void loadVehiculeLineFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				if(!sheet.getCell(1,row).getContents().trim().equals("")) {
    					
    					ER_Vehicle_Line oldLine = null;
        				if(!sheet.getCell(1,row).getContents().trim().equals("")) {
        					oldLine = ER_Vehicle_Line.find("name = ? and transferCode = ?", sheet.getCell(2,row).getContents(),sheet.getCell(1,row).getContents()).first();
        				}else {
        					oldLine = ER_Vehicle_Line.find("name = ? ", sheet.getCell(3,row).getContents()).first();
        				}
        				
        				if(oldLine==null) {
        					ER_Vehicle_Line  newLine = new ER_Vehicle_Line();
        					
        					newLine.transferCode = sheet.getCell(1,row).getContents();
        					
            				if(!sheet.getCell(2,row).getContents().trim().equals("")&&!sheet.getCell(3,row).getContents().trim().equals("")) {
            					
            					ER_Vehicle_Brand brand = ER_Vehicle_Brand.find("name", sheet.getCell(2,row).getContents()).first();
            					if(brand != null){
            						newLine.brand = brand;
            					}else{
            						brand = new ER_Vehicle_Brand();
            						brand.name = sheet.getCell(2,row).getContents();
            						newLine.brand = brand.save();
            					}
                        		newLine.name = sheet.getCell(3,row).getContents();
                        		newLine.vehicleClass = ER_Vehicle_Class.find("code like ?", "%"+sheet.getCell(4,row).getContents()+"%").first();
                        		newLine.insurable = "1".equals(sheet.getCell(5,row).getContents());
                        		if(!sheet.getCell(6,row).getContents().trim().equals("")) {
                        			newLine.loJackYear = Integer.valueOf(sheet.getCell(6,row).getContents());
                        		}
                        		
                        		if(!sheet.getCell(7,row).getContents().trim().equals("")){
                        			newLine.theftDeductible = new BigDecimal(sheet.getCell(7,row).getContents());
                        		}
                        		
                        		newLine.save();
                        		objectsCreated++;
                        		
            				}
                    		
        				}
    					
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("vehiculeLine.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("vehiculeLine.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("vehiculeLine.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("vehiculeLine.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newVehiculeLineFromExcel();
    	
    }
    
    public static void newVehiculeLineFromExcel() {
    	render();
    }

    /*
     * ************************************************************************************************************************
     * Vehicles LoJack list
     * ************************************************************************************************************************
     */

    public static void vehicleLoJack(Long lineId, Long brandId) {
        if (lineId!=null) {
            ER_Vehicle_Line line = ER_Vehicle_Line.findById(lineId);
            renderArgs.put("line", line);
        }

        filterVehicleLoJack(brandId,lineId);

        render();
    }

    private static void filterVehicleLoJack(Long brandId, Long lineId) {

        //Validate the parameters
        boolean validBrand = GeneralMethods.validateParameter(brandId);
        boolean validLine = GeneralMethods.validateParameter(lineId);

        //Create a new filter object and add the query for each valid parameter
        Filter filter = new Filter();

        if (validBrand) {
            filter.addQuery("line.brand.id = ?", brandId);
        }

        if (validLine) {
            filter.addQuery("line.id = ?", lineId);
        }

        //Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
        ModelPaginator vehicleLoJack = null;
        if (filter.isValidFilter()) {
            vehicleLoJack = new ModelPaginator(ER_Vehicle_LoJack.class, filter.getQuery(), filter.getParametersArray());
        } else {
            vehicleLoJack = new ModelPaginator(ER_Vehicle_LoJack.class);
        }

        vehicleLoJack.orderBy("line.brand.name, line.name, yearInit, yearEnd ASC");
        vehicleLoJack.setPageSize(20);
        renderArgs.put("vehicleLoJack", vehicleLoJack);
    }

    /*
     * ************************************************************************************************************************
     * Vehicles creation and editing
     * ************************************************************************************************************************
     */

    public static void editVehicleLoJack(Long id, Long lineId) {
        if (id!=null) {
            ER_Vehicle_LoJack vehicleLoJack = ER_Vehicle_LoJack.findById(id);
            renderArgs.put("vehicleLoJack", vehicleLoJack);

            if (vehicleLoJack.line!=null) {
                List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("brand.id = ? order by name", vehicleLoJack.line.brand.id).fetch();
                renderArgs.put("lines", lines);
            }
        }

        if (lineId!=null) {
            ER_Vehicle_Line line = ER_Vehicle_Line.findById(lineId);
            renderArgs.put("line", line);
        } else {
            List <ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("order by name").fetch();
            renderArgs.put("brands", brands);
        }

        if (flash.contains("brand.id") && id==null) {
            String brandIdString = flash.get("brand.id");
            if (brandIdString!=null && !brandIdString.isEmpty()) {
                Long brandId = Long.parseLong(brandIdString);
                List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("brand.id = ? order by name", brandId).fetch();
                renderArgs.put("lines", lines);
            }
        }

        render();
    }

    public static void saveVehicleLoJack(@Valid ER_Vehicle_LoJack vehicleLoJack, Long lineId) {
        flash.clear();

        if (validation.hasErrors()) {
            flash.error(Messages.get("vehicleLoJack.create.fielderrors"));
            params.flash();
            validation.keep();
            editVehicleLoJack(vehicleLoJack.id, lineId);
        } else {
            if (vehicleLoJack.id!=null) {
                ER_Vehicle_LoJack currentVehicleLoJack = ER_Vehicle_LoJack.findById(vehicleLoJack.id);
                currentVehicleLoJack.line = vehicleLoJack.line;
                currentVehicleLoJack.yearInit = vehicleLoJack.yearInit;
                currentVehicleLoJack.yearEnd = vehicleLoJack.yearEnd;
                currentVehicleLoJack.withLoJack = vehicleLoJack.withLoJack;
                currentVehicleLoJack.withoutLoJack = vehicleLoJack.withoutLoJack;
                currentVehicleLoJack.withoutLoJackPlus10 = vehicleLoJack.withoutLoJackPlus10;
                currentVehicleLoJack.withoutLoJackPlus15 = vehicleLoJack.withoutLoJackPlus15;
                currentVehicleLoJack.withoutLoJackPlusHalf = vehicleLoJack.withoutLoJackPlusHalf;
                currentVehicleLoJack.withoutLoJackPlusFull = vehicleLoJack.withoutLoJackPlusFull;
                currentVehicleLoJack.save();
                flash.success(Messages.get("vehicleLoJack.edit.success"));

            } else {

                ER_Vehicle_LoJack similarValue = ER_Vehicle_LoJack.find("line.id = ? and yearInit = ? and yearEnd = ?", vehicleLoJack.line.id, vehicleLoJack.yearInit, vehicleLoJack.yearEnd).first();

                if (similarValue==null) {
                    vehicleLoJack.save();
                    flash.success(Messages.get("vehicleLoJack.create.success"));
                } else {
                    params.flash();
                    flash.error(Messages.get("vehicleLoJack.create.duplicate"));
                    editVehicleLoJack(vehicleLoJack.id, lineId);
                }
            }

            if (lineId==null && vehicleLoJack.line!=null) {
                lineId = vehicleLoJack.line.id;
            }

            vehicleLoJack(lineId, null);
        }
    }

    /*
     * ************************************************************************************************************************
     * Vehicles LoJack massive
     * ************************************************************************************************************************
     */

    public static void vehicleLoJackMassiveUpdate() {
        render();
    }

    private static void processLoJackFile(File lojackFile) {

        Integer processedCount = 0;

        try {
            List<VehicleLoJack> values = ExcelBinder.bind(lojackFile, VehicleLoJack.class, 1);

            List<Map<String, Object>> errorValues = new ArrayList<Map<String, Object>>();

            int fileLine = 1;
            for (VehicleLoJack newValue : values) {
                fileLine++;

                if (!GeneralMethods.validateParameter(newValue.line) && !GeneralMethods.validateParameter(newValue.brand) ) {
                    continue;
                }

                ER_Vehicle_Brand brand = ER_Vehicle_Brand.find("name = ?", newValue.brand).first();
                ER_Vehicle_Line line = ER_Vehicle_Line.find("name = ? and brand.name = ?", newValue.line, newValue.brand).first();
                ER_Vehicle_LoJack value = null;
                if (newValue.yearInit != null) {
                    if (newValue.yearEnd != null) {
                        value = ER_Vehicle_LoJack.find("yearInit = ? and yearEnd = ? and line.name = ? and line.brand.name = ?", Integer.parseInt(newValue.yearInit), Integer.parseInt(newValue.yearEnd), newValue.line, newValue.brand).first();
                    } else {
                        value = ER_Vehicle_LoJack.find("yearInit = ? and yearEnd is null and line.name = ? and line.brand.name = ?", Integer.parseInt(newValue.yearInit), newValue.line, newValue.brand).first();
                    }
                } else {
                    if (newValue.yearEnd != null) {
                        value = ER_Vehicle_LoJack.find("yearInit is null and yearEnd = ? and line.name = ? and line.brand.name = ?", Integer.parseInt(newValue.yearEnd), newValue.line, newValue.brand).first();
                    } else {
                        value = ER_Vehicle_LoJack.find("yearInit is null and yearEnd is null and line.name = ? and line.brand.name = ?", newValue.line, newValue.brand).first();
                    }
                }

                Map<String, Object> errorMap = new HashMap<String, Object>();

                if (brand==null) {
                    errorMap.put("error", Messages.get("vehicleLoJack.massiveupdate.nobrand", newValue.brand));
                }
                if (line==null) {
                    errorMap.put("error", Messages.get("vehicleLoJack.massiveupdate.noline", newValue.line));
                }
                if (!errorMap.isEmpty()) {
                    errorMap.put("fileLine", fileLine);
                    errorMap.put("value", newValue);
                    errorValues.add(errorMap);
                    continue;
                }
                // If not from before, add new
                if (value==null) {
                    value = new ER_Vehicle_LoJack();
                    value.line = line;
                    if (newValue.yearInit != null) {
                        value.yearInit = Integer.parseInt(newValue.yearInit);
                    }
                    if (newValue.yearEnd != null) {
                        value.yearEnd = Integer.parseInt(newValue.yearEnd);
                    }
                }
                value.withLoJack = new BigDecimal(newValue.withLoJack.trim());
                value.withoutLoJack = new BigDecimal(newValue.withoutLoJack.trim());
                value.withoutLoJackPlus10 = new BigDecimal(newValue.withoutLoJackPlus10.trim());
                value.withoutLoJackPlus15 = new BigDecimal(newValue.withoutLoJackPlus15.trim());
                value.withoutLoJackPlusHalf = new BigDecimal(newValue.withoutLoJackPlusHalf.trim());
                value.withoutLoJackPlusFull = new BigDecimal(newValue.withoutLoJackPlusFull.trim());

                if (validation.valid(value).ok) {
                    value.save();
                    processedCount++;
                } else {
                    errorMap.put("error", Messages.get("vehicleLoJack.massiveupdate.allrequiredvalue"));
                    errorMap.put("fileLine", fileLine);
                    errorMap.put("value", newValue);
                    errorValues.add(errorMap);
                }

            }
            renderArgs.put("errorVehicleLoJack", errorValues);

        } catch (Exception e) {
            Logger.error(e, "Error processing car values file");
        }

        renderArgs.put("valuesProcessedCount", processedCount);
    }

    public static void uploadValuesLoJack(File lojackFile) {

        if (lojackFile!=null) {
            processLoJackFile(lojackFile);
            render();
        }

        flash.error(Messages.get("vehicleValue.masiveupdate.error"));
        vehicleValuesMassiveUpdate();
    }

}
