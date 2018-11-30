package models.ws.rest;

public class InspectionFinishRequest {

	private Long quotationNumber;
	private String inspectionNumber;
	private String existentDamage;
	private String soundEquipment;
	private String insurable;
	private String commonData;
	
	public Long getQuotationNumber() {
		return quotationNumber;
	}
	public void setQuotationNumber(Long quotationNumber) {
		this.quotationNumber = quotationNumber;
	}
	public String getInspectionNumber() {
		return inspectionNumber;
	}
	public void setInspectionNumber(String inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}
	public String getExistentDamage() {
		return existentDamage;
	}
	public void setExistentDamage(String existentDamage) {
		this.existentDamage = existentDamage;
	}
    public String getSoundEquipment() { return soundEquipment; }
    public void setSoundEquipment(String soundEquipment) { this.soundEquipment = soundEquipment; }
	public String getInsurable() { return insurable; }
	public void setInsurable(String insurable) { this.insurable = insurable; }
	public String getCommonData() {
		return commonData;
	}

	public void setCommonData(String commonData) {
		this.commonData = commonData;
	}
}
