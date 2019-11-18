package controllers;

import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import models.ER_Base_Field;
import models.ER_Channel;
import models.ER_Coverage;
import models.ER_Coverage_Category;
import models.ER_Currency;
import models.ER_Distributor;
import models.ER_Product;
import models.ER_Product_Coverage;
import models.ER_Product_Coverage_Class_Value;
import models.ER_Product_Coverage_Deductible;
import models.ER_Product_Coverage_Value;
import models.ER_Product_Discount;
import models.ER_Product_Discount_Range;
import models.ER_User_Role;
import models.ER_Vehicle_Class;
import models.ws.PolicyProductRequest;
import models.ws.PolicyProductResponse;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;
import service.ProductPolicyWebService;

import javax.inject.Inject;



@With(Secure.class)
@Check("Administrador maestro")
public class AdminProducts extends AdminBaseController {

	@Inject
	static ProductPolicyWebService productPolicyServiceBus;

	/*
	 * ************************************************************************************************************************
	 * Filters the users and adds the result as a ModelPaginator object with the key "products"
	 * ************************************************************************************************************************
	 */
	
	private static void filterProducts(String name, Boolean active) {
		//Validate the parameters
		boolean validName = GeneralMethods.validateParameter(name);
		boolean validState = GeneralMethods.validateParameter(active);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validName) {
			filter.addQuery("name like ?", "%"+name+"%");
		}
		
		if (validState) {
			filter.addQuery("active = ?", active, Operator.AND);
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
		
		//Validate the filter to create the model paginator. If the filter is invalid, all products are returned.
		ModelPaginator products = null;
		if (filter.isValidFilter()) {
			products = new ModelPaginator(ER_Product.class, filter.getQuery(), filter.getParametersArray());
		} else {
			products = new ModelPaginator(ER_Product.class);
		}
		
		products.setPageNumber(page);
		products.setPageSize(10);
		renderArgs.put("products", products);
	}
	
	/*
	 * ************************************************************************************************************************
	 * Products list
	 * ************************************************************************************************************************
	 */
	
	private static void productsList() {
		productsList(null, true, true);
	}
	
	public static void productsList(String name, Boolean active, boolean intern) {
		if (!intern) {
    		session.remove("page");
    		session.remove("name");
    		session.remove("active");
    		flash.clear();
    	}else{
    		params.flash();
    	}

    	//Add filtered users to the renderArgs
		filterProducts(name, active);
    	
    	//Add the http parameters to the flash scope
//    	params.flash();
    	
    	//Render the list
    	render();
    }
    
    public static void search(String name, Boolean active, String all, boolean intern, boolean internalSearch) {
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all users if the all button is pressed
    	if (!"".equals(all) && all!=null) {
    		session.remove("name");
    		session.remove("active");
    		
    		productsList(null, null, true);
    	}
    	
    	//List all users if the all button is pressed
    	if (all!=null) {
    		productsList(name, active, true);
    	}
    	
    	//Render the list
    	if(!intern) {
	    	session.put("name",name);
			session.put("active", active == null ? "todos" : active.booleanValue());
    	}
    	productsList(name, active, true);
    }
	
	/*
	 * ************************************************************************************************************************
	 * Product creation and editing
	 * ************************************************************************************************************************
	 */
    
    private static void renderEditProduct() {
    	
    	List<ER_Coverage_Category> categories = ER_Coverage_Category.findAll();
    	renderArgs.put("categories", categories);
    	
    	List<ER_Vehicle_Class> vehicleClasses = ER_Vehicle_Class.find("order by code").fetch();
    	renderArgs.put("vehicleClasses", vehicleClasses);
    	
    	List<ER_Base_Field> baseFields = ER_Base_Field.findAll();
    	renderArgs.put("baseFields", baseFields);
    	
    	List<ER_Currency> currencies = ER_Currency.find("active = ? order by code", true).fetch();
    	renderArgs.put("currencies", currencies);

    	renderTemplate("AdminProducts/editProduct.html");
    }
    
    public static void editProduct(Long id) {
    	
    	if (id!=null) {
    		ER_Product product = ER_Product.findById(id);
    		renderArgs.put("product", product);
    	}
    	
    	renderEditProduct();
    }
    
    public static void copyProduct(Long id) {
    	
    	if (id!=null) {
    		ER_Product product = ER_Product.findById(id);
    		renderArgs.put("product", product);
    	}
    	
    	render();
    }
    
    public static void loadProductFromExcel(File excelFile) {
    	
    	Workbook w;
    	try {
    		
    		String name;
    		String descripcion;
    		Long currencyId;
    		String remoteSystemCode;
    		boolean active;
    		boolean hasFacultative;
    		String aditionalBenefits;
    		
    		w = Workbook.getWorkbook(excelFile);
    		
    		Sheet sheet  = w.getSheet(0);
    		
    		name = sheet.getCell(2,3).getContents();
    		descripcion = sheet.getCell(2,4).getContents();
    		currencyId = Long.parseLong(sheet.getCell(2,5).getContents());
    		remoteSystemCode = sheet.getCell(2,6).getContents();
    		active = "1".equals(sheet.getCell(2,7).getContents());
    		hasFacultative = "1".equals(sheet.getCell(2,8).getContents());
    		aditionalBenefits = sheet.getCell(2,9).getContents();

    		ER_Product product = new ER_Product(null);
    		try {
				
				product.active = active;
				product.additionalBenefits = aditionalBenefits;
				product.currency = ER_Currency.findById(currencyId);
				product.description = descripcion;
				product.hasFacultative = hasFacultative;
				product.name = name;
				product.remoteSystemCode = remoteSystemCode;
				product.save();
				
				flash.success(Messages.get("product.create.success"));
			} catch (Exception e) {
				flash.error(Messages.get("product.create.fielderrors"));
				newProductFromExcel();
				e.printStackTrace();
			}    		

    		boolean next = true;
    		boolean same = false;
    		int startRow = 14;
    		List<ER_Vehicle_Class> vehicleClasses = ER_Vehicle_Class.find("order by code").fetch();
    		
    		Long coverageId;
    		BigDecimal minRange;
    		BigDecimal maxRange;
    		String optionName;
    		Long baseValueId;
    		boolean optional;
    		
    		
    		ER_Product_Coverage productCoverage = null;
    		
    		//iterate over rows
    		while (next) {

    			coverageId = getLongValue(sheet, startRow, 1);
    			minRange = getBigDecimalValue(sheet,startRow,3);
    			maxRange = getBigDecimalValue(sheet,startRow,4);
    			optionName = sheet.getCell(5,startRow).getContents();
    			baseValueId = Long.parseLong(sheet.getCell(6,startRow).getContents());
    			optional = "1".equals(sheet.getCell(7,startRow).getContents());
    					
    			if (coverageId == null) {
    				// No coverage defined
    				continue;
    			} else if (!same) {
    				productCoverage = new ER_Product_Coverage();
    				productCoverage.coverage =ER_Coverage.findById(coverageId);
        			productCoverage.optional = optional;
        			productCoverage.product = product;
        			productCoverage.valueBase = ER_Base_Field.findById(baseValueId);
        			productCoverage.save();
    			}
    			
    			ER_Product_Coverage_Value coverageValue = new ER_Product_Coverage_Value(null);
    			coverageValue.productCoverage = productCoverage;
				coverageValue.highRange = maxRange;
				coverageValue.lowRange = minRange;
				coverageValue.optionName = optionName;
				coverageValue.save();
				
    			int col = 7;
    			for (ER_Vehicle_Class vehicleClass : vehicleClasses) {
    				BigDecimal value = getBigDecimalValue(sheet, startRow, ++col);
    				
    				if (value == null) {
    					//No value defined by this class
    					continue;
    				}
    				
    				ER_Product_Coverage_Class_Value coverageClass = new ER_Product_Coverage_Class_Value(null);
    				coverageClass.vehicleClass = vehicleClass;
    				coverageClass.value = value;
    				coverageClass.productCoverageValue = coverageValue;
    				coverageClass.save();
    			}
    			
    			startRow++;
    			
    			Long nextCoverageid = getLongValue(sheet, startRow, 1);
    			
    			next = nextCoverageid != null;
    			same = coverageId == nextCoverageid;
    		}
    		
    	} catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	newProductFromExcel();
    }
    
    private static BigDecimal getBigDecimalValue(Sheet sheet, int row, int col) {
    	String cellValue = sheet.getCell(col,row).getContents();
    	return !"".equals(cellValue) && null != cellValue ? new BigDecimal(cellValue) : null;
    }
    
    private static Long getLongValue(Sheet sheet, int row, int col) {
    	String cellValue = sheet.getCell(col,row).getContents();
    	return !"".equals(cellValue) && null != cellValue ? Long.parseLong(cellValue) : null;
    }
    
    public static void newProductFromExcel(){
    	render();
    }
    public static void downloadNewProductTemplate() {

    	List<ER_Coverage_Category> categories = ER_Coverage_Category.findAll();
    	
    	renderArgs.put("categories", categories);
    	
    	List<ER_Vehicle_Class> vehicleClasses = ER_Vehicle_Class.find("order by code").fetch();
    	renderArgs.put("vehicleClasses", vehicleClasses);
//    	
    	List<ER_Base_Field> baseFields = ER_Base_Field.findAll();
    	renderArgs.put("baseFields", baseFields);
//    	
//    	List<ER_Currency> currencies = ER_Currency.find("active = ? order by code", true).fetch();
//    	renderArgs.put("currencies", currencies);
    	
    	request.format = "xls";
    	renderTemplate("Reports/NuevoProducto.xls");
    	
    }
    
    public static void saveProduct(@Valid ER_Product product, @Valid List<ER_Product_Coverage> coverages, String save) {
    	
    	flash.clear();
    	
    	if (coverages !=null) {
    		for (int i=0; i< coverages.size(); i++) {
    			ER_Product_Coverage coverage = coverages.get(i);
    			if (coverage.values!=null) {
    				for (int j=0; j< coverage.values.size(); j++) {
						ER_Product_Coverage_Value value = coverage.values.get(j);
						if (value.highRange!=null) {
							validation.min("coverages["+i+"].values["+j+"].highRange", value.highRange, value.lowRange.doubleValue());
						}
    				}
    			}
    		}
    	}
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("product.create.fielderrors"));
    		product.setCoveragesValues(coverages, false);
    		renderArgs.put("product", product);
    		renderArgs.put("validation", validation);
    		renderEditProduct();
    	} else {
    		
    		try {
	    		if (product.id != null) {
	    			product = ER_Product.findById(product.id);
	    		}
	    		
	    		product.currency = ER_Currency.findById(product.currency.id);
	    		
	    		product.save();
	    		product.setCoveragesValues(coverages, true);

	    		//ENVIO TRANSACCION A AS400 PARA GUARDAR U ACTUALIZAR
				if(product.policyFrom!= null && product.policyTo != null) {
					PolicyProductRequest request = new PolicyProductRequest();
					request.setId(product.id.toString());
					request.setStatus(product.active.toString());
					request.setName(product.name);
					request.setDescription(product.description);
					request.setPolicyFrom(product.policyFrom.toString());
					request.setPolicyTo(product.policyTo.toString());
					PolicyProductResponse queryAverage = productPolicyServiceBus.policyProductRequest(request);
					Logger.info("Actualiza AS400 producto " + queryAverage.getMessage());

					if (queryAverage.getCode().equals("1")) {
						flash.error("Mensaje de AS-400:" + queryAverage.getMessage());
					} else {
						flash.success("Operación exitosa");
					}
				}
				else
				flash.success("Operación exitosa");
    		} catch (Exception e) {
    			Logger.error(e, "Error saving product");
    			flash.success(Messages.get("product.save.error"));
    		}
    		
    		String name = session.get("name");
    		Boolean active = "todos".equals(session.get("active")) || null == session.get("active") ? null : Boolean.parseBoolean(session.get("active"));

    		if (name != null || active != null) {    			
    			search(name, active, "",true,true);
    		} else {
    	    	productsList(null, null,true);
    		}
    		
    		productsList();
    	}
	}

	public static void deleteProduct(Long id){

		System.out.print("paso 1");


		ER_Product  product  = null;
		if (id!=null) {
			product = ER_Product.findById(id);
		}
		System.out.print("paso 2");
		boolean errorscaptured = false;
		try
		{
			product.delete();
			flash.success(Messages.get("product.delete.success"));
		}
		catch ( Exception ex )
		{
			System.out.print("paso XXX");
			flash.error(Messages.get("product.delete.error"));
			errorscaptured = true;
			Logger.error(ex,"product: %d", id);

			System.out.print("paso YYYY");


		}
		System.out.print("paso 3 " + errorscaptured);
		if (errorscaptured)
		{

			String name = session.get("name");
			Boolean active = "todos".equals(session.get("active")) || null == session.get("active") ? null : Boolean.parseBoolean(session.get("active"));

			if (name != null || active != null) {
				productsList(name, active,true);
			} else {
				System.out.print("paso 5 " + errorscaptured);
				productsList(null, null, true);
			}
		}
		else
		{
			System.out.print("paso 6 " + errorscaptured);
			productsList(null, null,true);
		}
	}


	public static void saveProductCopy(@Required Long id, @Valid ER_Product product) {
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		validation.keep();
    		flash.error(Messages.get("product.copy.fielderrors"));
    		copyProduct(id);
    	} else {
    		try {
	    		ER_Product baseProduct = ER_Product.findById(id);
	    		if (baseProduct!=null) {
	    			ER_Product newProduct = new ER_Product(baseProduct);
	    			newProduct.name = product.name;
	    			newProduct.description = product.description;
	    			newProduct.remoteSystemCode = product.remoteSystemCode;
	    			newProduct.save();
	    			flash.success(Messages.get("product.copy.success"));
	    		} else {
	    			flash.error(Messages.get("product.copy.error"));
	    		}
    		} catch (Exception e) {
    			Logger.error(e, "Error saving product copy");
    			flash.success(Messages.get("product.save.error"));
    		}
    		
    		productsList();
    	}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Product additional parameters
	 * ************************************************************************************************************************
	 */
    
    public static void productDeductibles(Long id) {
    	renderProductDeductibles(id);
    }
    
    private static void renderProductDeductibles(Long id) {
    	ER_Product product = ER_Product.findById(id);
    	renderArgs.put("product", product);
    	
    	List<ER_Product_Coverage> coverages = ER_Product_Coverage.find("product = ? order by coverage.category.code", product).fetch();
    	renderArgs.put("coverages", coverages);
    	
    	List<ER_Vehicle_Class> vehicleClasses = ER_Vehicle_Class.find("order by code").fetch();
    	renderArgs.put("vehicleClasses", vehicleClasses);
    	
    	if (product!=null && coverages!=null && vehicleClasses!=null) {
    		renderTemplate("AdminProducts/productDeductibles.html");
    	}
    	
    	productsList();
    }
    
//    public static void productAdditionalParameters(Long id) {
//    	try {
//	    	ER_Product product = ER_Product.findById(id);
//	    	renderArgs.put("product", product);
//	    	
//	    	List<ER_Product_Coverage> coverages = ER_Product_Coverage.find("product = ? order by coverage.category.code", product).fetch();
//	    	renderArgs.put("coverages", coverages);
//	    	
//    	} catch (Exception e) {
//    		Logger.error(e, "Error listing product's additional parameters");
//    		productsList();
//    	}
//    	
//    	render();
//    	
//    }
    
    public static void saveDeductibles(Long productId, @Valid List<ER_Product_Coverage> coverages, @Valid List<ER_Product_Coverage_Deductible> deductibles) {
    	
    	flash.clear();
    	
    	if (productId==null) {
    		productsList();
    	}
    	
    	if (validation.hasErrors()) {
    		renderArgs.put("data", params.allSimple());
    		flash.error(Messages.get("product.deductibles.error"));
    		renderProductDeductibles(productId);
    	} else {
	    	for (ER_Product_Coverage coverage : coverages) {
	    		ER_Product_Coverage currentCoverage = ER_Product_Coverage.findById(coverage.id);
	    		currentCoverage.minimumDeductible = coverage.minimumDeductible;
	    		currentCoverage.minimumPrime = coverage.minimumPrime;
	    		currentCoverage.coverageValue = coverage.coverageValue;
	    		currentCoverage.save();
	    	}
	    	
	    	for (ER_Product_Coverage_Deductible deductible : deductibles)  {
	    		if (deductible.id!=null) {
	    			ER_Product_Coverage_Deductible currentDeductible = ER_Product_Coverage_Deductible.findById(deductible.id);
	    			currentDeductible.damagesDeductible = deductible.damagesDeductible;
	    			currentDeductible.save();
	    		} else {
	    			deductible.save();
	    		}
	    	}
	    	flash.success(Messages.get("product.deductibles.save.success"));
	    	productsList();
    	}
    }
    
//    public static void saveAdditionalParameters(Long productId, @Valid List<ER_Product_Coverage> coverages) {
//    	
//    	flash.clear();
//    	
//    	if (productId==null) {
//    		productsList();
//    	}
//    	
//    	if (validation.hasErrors()) {
//    		validation.keep();
//    		flash.error(Messages.get("product.parameters.error"));
//    		productAdditionalParameters(productId);
//    	} else {
//    		for (ER_Product_Coverage coverage : coverages) {
//	    		
//	    		ER_Product_Coverage currentCoverage = ER_Product_Coverage.findById(coverage.id);
//	    		currentCoverage.applyDiscount = coverage.applyDiscount;
//	    		currentCoverage.applyEmissionFee = coverage.applyEmissionFee;
//	    		currentCoverage.applyFractioningFee = coverage.applyFractioningFee;
//	    		currentCoverage.applyVAT = coverage.applyVAT;
//	    		currentCoverage.applyInNetPrime = coverage.applyInNetPrime;
//	    		currentCoverage.save();
//	    	}
//	    	
//	    	flash.success(Messages.get("product.parameters.save.success"));
//	    	productsList();
//    	}
//    }
    
    /*
	 * ************************************************************************************************************************
	 * Product permissions
	 * ************************************************************************************************************************
	 */
    
    public static void productChannelPermissions(Long id) {
    	
    	if (id!=null) { 
	    	ER_Product product = ER_Product.findById(id);
	    	List<ER_Channel> channels = ER_Channel.find("active = true order by name").fetch();
	    	if (product!=null && product.channels!=null) {
	    		channels.removeAll(product.channels);
	    	}
	    	
	    	renderArgs.put("assigned", product.channels);
	    	render(product, channels);
    	}
    	
    	productsList();
    }
    
    public static void saveChannelPermissions(Long productId, Long[] channels) {
    	flash.clear();
    	
    	try {
    		
    		ER_Product product = ER_Product.findById(productId);
    		product.channels = null;
    		product.save();
    		
    		if (channels!=null) {
    			List<ER_Channel> assignedChannels = new ArrayList<ER_Channel>();
    			for (Long channelId : channels) {
    				ER_Channel channel = ER_Channel.findById(channelId);
    				assignedChannels.add(channel);
    			}
    			product.channels = assignedChannels;
        		product.save();
    		}
    		
    		
    		flash.success(Messages.get("product.channelpermissions.success"));
    		productsList();
    		
    	} catch (Exception e) {
    		flash.error(Messages.get("product.channelpermissions.error"));
    		Logger.error(e, "Error saving channel's permissions");
    		productChannelPermissions(productId);
    	}
    }
    
    public static void productDistributorPermissions(Long id) {
    	
    	if (id!=null) { 
    		ER_Product product = ER_Product.findById(id);
        	List<ER_Distributor> distributors = ER_Distributor.find("active = true order by channel.name, name asc").fetch();
        	if (product!=null && product.distributors!=null) {
        		distributors.removeAll(product.distributors);
        	}
        	
        	renderArgs.put("assigned", product.distributors);
        	render(product, distributors);
    	}
    	
    	productsList();

    }
    
    public static void saveDistributorPermissions(Long productId, Long[] distributors) {
    	flash.clear();
    	
    	try {
    		ER_Product product = ER_Product.findById(productId);
    		product.distributors = null;
    		product.save();
    		
    		if (distributors!=null) {
    			List<ER_Distributor> assignedDistributors = new ArrayList<ER_Distributor>();
    			for (Long distributorId : distributors) {
    				ER_Distributor distributor = ER_Distributor.findById(distributorId);
    				assignedDistributors.add(distributor);
    			}
    			product.distributors = assignedDistributors;
        		product.save();
    		}
    		
    		
    		flash.success(Messages.get("product.distributorpermissions.success"));
    		productsList();
    		
    	} catch (Exception e) {
    		flash.error(Messages.get("product.distributorpermissions.error"));
    		Logger.error(e, "Error saving distributors permissions");
    		productDistributorPermissions(productId);
    	}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Product discounts
	 * ************************************************************************************************************************
	 */
    
    public static void productDiscounts(Long id) {
    	
    	if (id!=null) { 
	    	ER_Product product = ER_Product.findById(id);
	    	if (product!=null) {
	    			
	    		//Create a list of product discount objects with values from the table and defaults if not set
	    		List<ER_Product_Discount> discounts = new ArrayList<ER_Product_Discount>();
	    		List<ER_User_Role> roles = ER_User_Role.find("code != ? order by name", ERConstants.USER_ROLE_SUPER_ADMIN).fetch();
	    		for (ER_User_Role role: roles) {
	    			ER_Product_Discount discount = null;
	    			if (product.discounts!=null) {
	    				int i = 0;
	    				while (discount==null && i< product.discounts.size()) {
	    					ER_Product_Discount currentDiscount = product.discounts.get(i);
	    					if (currentDiscount.role.equals(role)) {
	    						discount = currentDiscount;
	    					}
	    					i++;
	    				}
	    			}
	    			
	    			if (discount==null) {
	    				discount = new ER_Product_Discount();
	    				discount.role = role;
	    			}
	    			
	    			discounts.add(discount);
	    		}
	    		
	    		renderArgs.put("discounts", discounts);
	    		
    			if (product.rangeDiscounts==null || product.rangeDiscounts.isEmpty()) {
    				List<ER_Product_Discount_Range> rangeDiscounts = new ArrayList<ER_Product_Discount_Range>();
    				ER_Product_Discount_Range discount = new ER_Product_Discount_Range();
    				discount.lowRange = BigDecimal.ZERO;
    				discount.value = BigDecimal.ZERO;
    				rangeDiscounts.add(discount);
    				renderArgs.put("rangeDiscounts", rangeDiscounts);
    			} else {
    				
    				List<ER_Product_Discount_Range> rangeDiscounts = product.rangeDiscounts;
    				Collections.sort(rangeDiscounts, new Comparator<ER_Product_Discount_Range>(){
						@Override
						public int compare(ER_Product_Discount_Range arg0,ER_Product_Discount_Range arg1) {
							return arg0.lowRange.compareTo(arg1.lowRange);
						}
    				});
    				
    				renderArgs.put("rangeDiscounts", rangeDiscounts);
    			}
    		
    			render(product);
	    	}
    	}
    	
    	productsList();
    }
    
    public static void saveProductDiscounts(List<ER_Product_Discount> discounts, List<ER_Product_Discount_Range> rangeDiscounts, Boolean discountedByRange, @Required Long productId) {
    	flash.clear();
    	
    	if (discountedByRange) {
    		validation.valid(rangeDiscounts);
    		validation.valid(rangeDiscounts);
    	} else {
    		validation.required(discounts);
    		validation.valid(discounts);
    	}
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		validation.keep();
    		productDiscounts(productId);
    	} else {
    		try {
        		ER_Product product = ER_Product.findById(productId);
        		product.discountedByRange = discountedByRange;
        		
        		if (!discountedByRange) {
        			
        			product.rangeDiscounts.clear();
        			
        			List<ER_Product_Discount> newDiscounts = new ArrayList<ER_Product_Discount>();
        			
        			if (discounts!=null && !discounts.isEmpty()) {
		        		for (ER_Product_Discount discount : discounts) {
		        			BigDecimal value = discount.value;
		        			if (discount.id!=null) {
		        				discount = product.discountById(discount.id);
		        			}
		        			
		        			discount.value = value;
		        			discount.product = product;
		        			
		        			newDiscounts.add(discount);
						}
        			}
	        		
	        		product.discounts.clear();
	        		product.discounts.addAll(newDiscounts);
	        		
        		} else {
        			
        			product.discounts.clear();
        			
        			List<ER_Product_Discount_Range> newRangeDiscounts = new ArrayList<ER_Product_Discount_Range>();
        			
        			if (rangeDiscounts!=null && !rangeDiscounts.isEmpty()) {
	        			for (ER_Product_Discount_Range discount : rangeDiscounts) {
		        			BigDecimal value = discount.value;
		        			BigDecimal lowRange = discount.lowRange;
		        			BigDecimal highRange = discount.highRange;
		        			if (discount.id!=null) {
		        				discount = product.discountRangeById(discount.id);
		        			}
		        			
		        			if (discount!=null) {
		        				discount.lowRange = lowRange;
		        				discount.highRange = highRange;
			        			discount.value = value;
			        			discount.product = product;
			        			
			        			newRangeDiscounts.add(discount);
		        			}
						}
        			}
        			
        			product.rangeDiscounts.clear();
	        		product.rangeDiscounts.addAll(newRangeDiscounts);
        		}
        		
        		product.save();
        		
        		flash.success(Messages.get("product.productdiscounts.success"));
        		productsList();
        		
        	} catch (Exception e) {
        		flash.error(Messages.get("product.productdiscounts.error"));
        		Logger.error(e, "Error saving product's discounts");
        		productDiscounts(productId);
        	}
    	}
    }
}
