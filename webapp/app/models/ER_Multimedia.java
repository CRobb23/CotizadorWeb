package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class ER_Multimedia extends Model{

	@Column(name="has_consolidated")
	public Boolean hasConsolidated;
	@Column(name="url_consolidated")
	public String urlConsolidated;
	@Column(name="url_dpi")
	public String urlDPI;
	@Column(name="url_receipt_services")
	public String urlReceiptServices;
	@Column(name="url_circulation_card")
	public String urlCirculationCard;
	@Column(name="url_driver_license")
	public String urlDriverLicence;
	@Column(name="url_car_invoice_new")
	public String urlCarInvoiceNew;
	@Column(name="url_another_company_policy")
	public String urlAnotherCompanyPolicy;
	@Column(name="url_deposit_receipt")
	public String urlDepositReceipt;
	@Column(name="url_authorization_form")
	public String urlAuthorizationForm;
	
	/** Business Client */
	@Column(name="url_scan_patents")
	public String urlScanPatents;
	@Column(name="url_scan_legal_representative_appointment")
	public String urlScanLegalRepresentativeAppointment;
	@Column(name="url_dpi_legal_representative")
	public String urlDPILegalRepresentative;
    @Column(name="url_rtu")
    public String urlRTU;
    @Column(name="url_receipt_services_legal")
    public String urlReceiptServicesLegal;

	@Column(name="uploaded_files_gd")
	public Boolean uploadedFilesGD;

	@Column(name="can_upload_files")
	public Boolean canUploadFiles;

	@Column(name="has_more_files")
	public Boolean hasAditionalFilesGD;

	/** Forms */
	@Column(name="url_form_request_auto")
	public String urlformRequestAuto;
	@Column(name="url_form_ive")
	public String urlFormIVE;
	@Column(name="url_form_pep")
	public String urlFormPEP;
	@Column(name="url_form_payerpep")
	public String urlFormPayerPEP;

	
	
	public Boolean isLocalFile(String urlFile){
		return (urlFile != null && !urlFile.contains("http") && !urlFile.contains("https"));
	}
}