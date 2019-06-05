package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;

import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.StringUtil;

@Entity
public class ER_Vehicle extends Model {
	
	@ManyToOne
	public ER_Vehicle_Line line;
	public Integer year;
	@MaxSize(10)
	@Column(length=10)
	public String plate;
	@Min(1)
	public Integer numberOfPassengers;
	public Boolean alreadyInsured;

	public Boolean quotationnNew;

	public Date invoiceDate;
	
	/** Fields phase II */
	@Column(name="chassis")
    @MinSize(17)
    @MaxSize(17)
	public String chassis;
	@Column(name="engine")
	public String engine;
	@Column(name="armor")
	public String armor;
	@Column(name="armor_value")
	public String armorValue;
	@Column(name="color")
	public String color;
	@XmlElement(name="cubic_centimeter")
	public Integer cubicCentimeter;
	@XmlElement(name="number_cylinders")
	public Integer numberCylinders;
	@XmlElement(name="number_axes")
	public Integer numberAxes;
	@XmlElement(name="number_door")
	public String numberDoor;
	@XmlElement(name="helmet_class")
	public Long helmetClass;
	@Column(name="mileage")
	public String mileage;
	@XmlElement(name="type_mileage")
	public String typeMileage;
	@XmlElement(name="fuel_type")
	public String fuelType;
	@XmlElement(name="tonelaje")
	public Integer tonnage;
	@Column(name="loan_number")
	public String loanNumber;
	@Column(name="warranty")
	public String warranty;
	@XmlElement(name="selection")
	public String selection;
	@Column(name="is_new")
	public Boolean isNew;
	@Column(name="inspection_number")
	public String inspectionNumber;
	@XmlElement(name="pre_existing_damage")
	public String preExistingDamage;
	@XmlElement(name="promotion_code")
	public String promotionCode;
	@XmlElement(name="special_team")
	public String specialTeam;

    public BigDecimal averageValue;

    public String owner;

    /** Relations */
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_type", nullable=true)
    @Required
	public ER_Vehicle_Type type;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_rate", nullable=true)
	public ER_Rate rate;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_year", nullable=true)
	public ER_Year erYear;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_reminder_type", nullable=true)
	public ER_Reminder_Type reminderType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_plate_type", nullable=true)
	public ER_Plate_Type plateType;
    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="id_beneficiaries", nullable=true)
    public ER_Beneficiaries beneficiaries;

	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.year!=null) {
			map.put("year", this.year);
		}
		if (this.plate!=null) {
			map.put("plate", this.plate);
		}
		if (this.numberOfPassengers!=null) { 
			map.put("numberOfPassengers", this.numberOfPassengers);
		}
		if (this.alreadyInsured!=null) { 
			map.put("alreadyInsured", this.alreadyInsured);
		}
		if (this.line!=null) {
			map.put("line", line.toMap());
		}
		/*if (this.invoiceDate !=null) {
			map.put("line", invoiceDate.toMap());
		}*/
		
		return map;
	}
	
	public String description() {
		String description = "";
		if (line!=null && line.brand!=null) {
			description += line.brand.name + " " + line.name;
		}
		
		if (erYear!=null) {
			description += " " + erYear.year;
		}
		
		return description;
	}
	
	public boolean requiresLoJack() {
		return this.line!=null && this.line.loJackYear!=null && this.year!=null && this.line.loJackYear <= this.year;
	}

	public String getFullPlate() {
	    if (plateType != null && !StringUtil.isNullOrBlank(plate)) {
	        return (plateType.transferCode + plate);
        }
        return "";
    }
	public void ConvertUpper(){

		if (!StringUtil.isNullOrBlank(this.plate)) {
			this.plate = this.plate.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.chassis)) {
			this.chassis = this.chassis.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.engine)) {
			this.engine = this.engine.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.armor)) {
			this.armor = this.armor.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.armorValue)) {
			this.armorValue = this.armorValue.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.color)) {
			this.color = this.color.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.mileage)) {
			this.mileage = this.mileage.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.fuelType)) {
			this.fuelType = this.fuelType.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.typeMileage)) {
			this.typeMileage = this.typeMileage.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.warranty)) {
			this.warranty = this.warranty.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.inspectionNumber)) {
			this.inspectionNumber = this.inspectionNumber.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.preExistingDamage)) {
			this.preExistingDamage = this.preExistingDamage.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.promotionCode)) {
			this.promotionCode = this.promotionCode.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.specialTeam)) {
			this.specialTeam = this.specialTeam.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.owner)) {
			this.owner = this.owner.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.preExistingDamage)) {
			this.preExistingDamage = this.preExistingDamage.toUpperCase();
		}
	}

}
