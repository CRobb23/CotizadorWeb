package models.ws.rest;

public class Inspection {
	
	private String inspectionType;
	private String date;
	private String inspectionLocation;
	private String clientName;
	private String address;
	private Long quotationNumber;
	private String contactEmail;
	private String contactPhone;
	private Long brokerId;
	
	public String getInspectionType() {
		return inspectionType;
	}
	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getInspectionLocation() {
		return inspectionLocation;
	}
	public void setInspectionLocation(String inspectionLocation) {
		this.inspectionLocation = inspectionLocation;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getQuotationNumber() {
		return quotationNumber;
	}
	public void setQuotationNumber(Long quotationNumber) {
		this.quotationNumber = quotationNumber;
	}

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}
}
