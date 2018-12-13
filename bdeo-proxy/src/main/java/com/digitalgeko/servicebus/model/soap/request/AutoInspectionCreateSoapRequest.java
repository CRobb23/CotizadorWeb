package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="crearautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapRequest {

    public static final String RQ_CODE = "786";
    public static final String RS_CODE = "787";

    @XmlElement(name="caso")
    private String caseNumber;
    @XmlElement(name="telefono")
    private String phone;
    @XmlElement(name="nombreAsegurado")
    private String insuredName;
    @XmlElement(name="apellidoAsegurado")
    private String insuredSurname;
    @XmlElement(name="dpi")
    private String identificationDocument;
    @XmlElement(name="placa")
    private String plate;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getInsuredSurname() {
        return insuredSurname;
    }

    public void setInsuredSurname(String insuredSurname) {
        this.insuredSurname = insuredSurname;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
