package models;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class ER_Payment extends Model {
	
	@Column(name="number_account_card", nullable=true)
	public String numberAccountCard;
	@Column(name="code_account_card", nullable=true)
	public String codeAccountCard;
	@Column(name="expiration_date", nullable=true)
	public Date expirationDate;

    @Column(name="isAgree")
    public Boolean isAgree;
	
	@OneToOne(mappedBy="payment")
	public ER_Incident incident;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_charge_type", nullable=true)
	public ER_Charge_Type chargeType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_payment_type", nullable=true)
	public ER_Payment_Type paymentType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_bank_account_type", nullable=true)
	public ER_Bank_Account_Type bankAccountType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_bank", nullable=true)
	public ER_Bank bank;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_card_type", nullable=true)
	public ER_Card_Type cardType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_card_class", nullable=true)
	public ER_Card_Class cardClass;

	public String getFullChargeType() {
	    if (chargeType != null) {
	        String fullChargeType = "";
            switch (chargeType.transferCode) {
                case "02": // DEBITO A CUENTA
                    fullChargeType = "débito bi";
                    break;
                case "03": // DEBITO A TARJETA
                    fullChargeType = "tarjeta de crédito";
                    break;
                case "05": // COBRADOR
                    fullChargeType = "cobrador";
                    break;
                case "04": // COBRADOR
                    fullChargeType = "boletas y convenios";
                    break;
            }
            return fullChargeType;
        }
        return "";
    }

    public String getFullChargeMedia() {
	    if (chargeType != null) {
            String fullChargeMedia = "";
            switch (chargeType.transferCode) {
                case "02": // DEBITO A CUENTA
                    if (bankAccountType != null) {
                        switch (bankAccountType.transferCode) {
                            case "A":
                                fullChargeMedia = "cuenta ahorros";
                                break;
                            case "M":
                                fullChargeMedia = "cuenta monetarios";
                                break;
                        }
                    }
                    break;
                case "03": // DEBITO A TARJETA
                    fullChargeMedia = "tarjeta";
                    break;
            }
            return fullChargeMedia;
        }
        return "";
    }

    public String getExpirationMonth() {
	    if (expirationDate != null) {
            SimpleDateFormat to = new SimpleDateFormat("MM");
            return to.format(expirationDate);
        }
        return "";
    }

    public String getExpirationYear() {
        if (expirationDate != null) {
            SimpleDateFormat to = new SimpleDateFormat("yyyy");
            return to.format(expirationDate);
        }
        return "";
    }

}
