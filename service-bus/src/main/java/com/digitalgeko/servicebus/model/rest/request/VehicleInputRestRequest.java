package com.digitalgeko.servicebus.model.rest.request;

import java.math.BigDecimal;
import java.util.List;

public class VehicleInputRestRequest {

	private String quoteNumber;
	private String currency;
	private String chasis;
	private String engine;
	private String plate;
	private String brand;
	private String line;
	private String typeVehicle;
	private String rateType;
	private String armor;
	private String armorValue;
	private String reminderCode;
	private String year;
	private Integer numberOfPassengers;
	private String color;
	private Integer cubicCentimeter;
	private Integer numberCylinders;
	private Integer numberAxes;
	private String numberDoor;
	private Long helmetClass;
	private BigDecimal vehicleValue;
	private String mileage;
	private String typeMileage;
	private String fuelType;
	private Integer tonnage;
	private String loanNumber;
	private String warranty;
	private String beneficiaryCodeWarranty;
	private String selection;
	private String inspectionNumber;
	private String preExistingDamage;
	private String promotionCode;
	private String specialTeam;
    private String onlyThird;
	private String vehicleOwner;
	private List<Damage> damageList;
	private List<SpecialTeam> specialTeamList;
	
    public static class Damage{
		private Integer numberLine;
		private String descriptionLine;
		
		public Integer getNumberLine() {
			return numberLine;
		}
		public void setNumberLine(Integer numberLine) {
			this.numberLine = numberLine;
		}
		public String getDescriptionLine() {
			return descriptionLine;
		}
		public void setDescriptionLine(String descriptionLine) {
			this.descriptionLine = descriptionLine;
		}
	}
	
    public static class SpecialTeam{
		private Integer numberLine;
		private String descriptionLine;

        public Integer getNumberLine() {
            return numberLine;
        }
        public void setNumberLine(Integer numberLine) {
            this.numberLine = numberLine;
        }
        public String getDescriptionLine() {
            return descriptionLine;
        }
        public void setDescriptionLine(String descriptionLine) {
            this.descriptionLine = descriptionLine;
        }
	}
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getChasis() {
		return chasis;
	}
	public void setChasis(String chasis) {
		this.chasis = chasis;
	}
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getTypeVehicle() {
		return typeVehicle;
	}
	public void setTypeVehicle(String typeVehicle) {
		this.typeVehicle = typeVehicle;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public String getArmor() {
		return armor;
	}
	public void setArmor(String armor) {
		this.armor = armor;
	}
	public String getArmorValue() {
		return armorValue;
	}
	public void setArmorValue(String armorValue) {
		this.armorValue = armorValue;
	}
	public String getReminderCode() {
		return reminderCode;
	}
	public void setReminderCode(String reminderCode) {
		this.reminderCode = reminderCode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public Integer getNumberOfPassengers() {
		return numberOfPassengers;
	}
	public void setNumberOfPassengers(Integer numberOfPassengers) {
		this.numberOfPassengers = numberOfPassengers;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getCubicCentimeter() {
		return cubicCentimeter;
	}
	public void setCubicCentimeter(Integer cubicCentimeter) {
		this.cubicCentimeter = cubicCentimeter;
	}
	public Integer getNumberCylinders() {
		return numberCylinders;
	}
	public void setNumberCylinders(Integer numberCylinders) {
		this.numberCylinders = numberCylinders;
	}
	public Integer getNumberAxes() {
		return numberAxes;
	}
	public void setNumberAxes(Integer numberAxes) {
		this.numberAxes = numberAxes;
	}
	public String getNumberDoor() {
		return numberDoor;
	}
	public void setNumberDoor(String numberDoor) {
		this.numberDoor = numberDoor;
	}
	public Long getHelmetClass() {
		return helmetClass;
	}
	public void setHelmetClass(Long helmetClass) {
		this.helmetClass = helmetClass;
	}
	public BigDecimal getVehicleValue() {
		return vehicleValue;
	}
	public void setVehicleValue(BigDecimal vehicleValue) {
		this.vehicleValue = vehicleValue;
	}
	public String getMileage() {
		return mileage;
	}
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getTypeMileage() {
		return typeMileage;
	}
	public void setTypeMileage(String typeMileage) {
		this.typeMileage = typeMileage;
	}
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public Integer getTonnage() {
		return tonnage;
	}
	public void setTonnage(Integer tonnage) {
		this.tonnage = tonnage;
	}
	public String getLoanNumber() {
		return loanNumber;
	}
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}
	public String getWarranty() {
		return warranty;
	}
	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}
	public String getBeneficiaryCodeWarranty() {
		return beneficiaryCodeWarranty;
	}
	public void setBeneficiaryCodeWarranty(String beneficiaryCodeWarranty) {
		this.beneficiaryCodeWarranty = beneficiaryCodeWarranty;
	}
	public String getSelection() {
		return selection;
	}
	public void setSelection(String selection) {
		this.selection = selection;
	}
	public String getInspectionNumber() {
		return inspectionNumber;
	}
	public void setInspectionNumber(String inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}
	public String getPreExistingDamage() {
		return preExistingDamage;
	}
	public void setPreExistingDamage(String preExistingDamage) {
		this.preExistingDamage = preExistingDamage;
	}
	public String getPromotionCode() {
		return promotionCode;
	}
	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}
	public String getSpecialTeam() {
		return specialTeam;
	}
	public void setSpecialTeam(String specialTeam) {
		this.specialTeam = specialTeam;
	}
	public List<Damage> getDamageList() {
		return damageList;
	}
	public void setDamageList(List<Damage> damageList) {
		this.damageList = damageList;
	}
	public List<SpecialTeam> getSpecialTeamList() {
		return specialTeamList;
	}
	public void setSpecialTeamList(List<SpecialTeam> specialTeamList) {
		this.specialTeamList = specialTeamList;
	}

    public String getOnlyThird() {
        return onlyThird;
    }

    public void setOnlyThird(String onlyThird) {
        this.onlyThird = onlyThird;
    }

	public String getVehicleOwner() {
		return vehicleOwner;
	}

	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}
}
