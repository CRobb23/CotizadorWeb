package models.ws;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="datosdeVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class VehicleRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 735;
	
	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="moneda")
	private String currency;
	@XmlElement(name="chasis")
	private String chasis;
	@XmlElement(name="motor")
	private String engine;
	@XmlElement(name="placa")
	private String plate;
	@XmlElement(name="marca")
	private String brand;
	@XmlElement(name="marcaLinea")
	private String line;
	@XmlElement(name="tipoVehiculo")
	private String typeVehicle;
	@XmlElement(name="tipoTarifa")
	private String rateType;
	@XmlElement(name="blindaje")
	private String armor;
	@XmlElement(name="valorBlindaje")
	private String armorValue;
	@XmlElement(name="codigoAlarma")
	private String reminderCode;
	@XmlElement(name="modeloVehiculo")
	private String year;
	@XmlElement(name="numeroPasajeros")
	private Integer numberOfPassengers;
	@XmlElement(name="color")
	private String color;
	@XmlElement(name="centimetrosCubicos")
	private Integer cubicCentimeter;
	@XmlElement(name="numeroCilindros")
	private Integer numberCylinders;
	@XmlElement(name="numeroEjes")
	private Integer numberAxes;
	@XmlElement(name="numeroPuertas")
	private String numberDoor;
	@XmlElement(name="claseCasco")
	private Long helmetClass;
	@XmlElement(name="valorVehiculo")
	private BigDecimal vehicleValue;
	@XmlElement(name="kilometraje")
	private String mileage;
	@XmlElement(name="tipoKilometraje")
	private String typeMileage;
	@XmlElement(name="tipoCombustible")
	private String fuelType;
	@XmlElement(name="tonelaje")
	private Integer tonnage;
	@XmlElement(name="numeroPrestamo")
	private String loanNumber;
	@XmlElement(name="garantia")
	private String warranty;
	@XmlElement(name="codigoBeneficiarioGarantia")
	private String beneficiaryCodeWarranty;
	@XmlElement(name="seleccion")
	private String selection;
	@XmlElement(name="numeroInspeccion")
	private String inspectionNumber;
	@XmlElement(name="danosPreexistentes")
	private String preExistingDamage;
	@XmlElement(name="codigoPromocion")
	private String promotionCode;
	@XmlElement(name="equipoEspecial")
	private String specialTeam;
    @XmlElement(name="terceros")
    private String onlyThird;
	@XmlElement(name="propietario")
	private String vehicleOwner;
    @XmlElementWrapper(name="listaDanos")
    @XmlElement(name="lista")
	private List<Damage> damageList;
    @XmlElementWrapper(name="listaEquipoEspecial")
    @XmlElement(name="listaEquipo")
	private List<SpecialTeam> specialTeamList;
	
	@XmlAccessorType(XmlAccessType.FIELD)
    public static class Damage{
		@XmlElement(name="numeroLinea")
		private Integer numberLine;
		@XmlElement(name="descripcionLinea")
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
	
	@XmlAccessorType(XmlAccessType.FIELD)
    public static class SpecialTeam{
		@XmlElement(name="numeroLineaEE")
		private Integer numberLine;
		@XmlElement(name="descripcion")
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
