package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="crearautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapResponse {

    @XmlElement(name="id")
    private String id;
    @XmlElement(name="caso")
    private String caseId;
    @XmlElement(name="casoRef")
    private String caseRef;
    @XmlElement(name="direccion")
    private String address;
    @XmlElement(name="empresaId")
    private String masterCompanyId;
    @XmlElement(name="empresa")
    private String companyName;
    @XmlElement(name="abrioEnlace")
    private Boolean isOpen;
    @XmlElement(name="estado")
    private Integer status;
    @XmlElement(name="comentarios")
    private String comments;
    @XmlElement(name="creadoId")
    private String createdById;
    @XmlElement(name="creadoNombre")
    private String createdByName;
    @XmlElement(name="actualizadoId")
    private String updatedById;
    @XmlElement(name="creadoFecha")
    private Long createdDate;
    @XmlElement(name="actualizadoFecha")
    private Long updatedDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMasterCompanyId() {
        return masterCompanyId;
    }

    public void setMasterCompanyId(String masterCompanyId) {
        this.masterCompanyId = masterCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

}
