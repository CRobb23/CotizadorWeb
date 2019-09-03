package models;


import play.db.jpa.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class ER_ReviewDetail extends Model {

    public Date declineDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User declineUser;

    public Date acceptanceDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User acceptanceUser;

    public Date technicianTransferDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User tecchnicianTransferUser;

    public Date comercialTransferDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User comercialTransferUser;

    public Date reviewDate;
    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User reviewUser;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="status_id", nullable=true)
    public ER_ReviewStatus status;

    public String getDeclineUser() {
        if(this.declineUser != null)
            return declineUser.getFullName();
        else
            return " ";
    }

    public String getAcceptanceUser() {
        if(this.acceptanceUser != null)
            return acceptanceUser.getFullName();
        else
            return " ";
    }

    public String getTecchnicianTransferUser() {
        if(this.tecchnicianTransferUser != null)
            return tecchnicianTransferUser.getFullName();
        else
            return " ";
    }

    public String getComercialTransferUser() {
        if(this.comercialTransferUser != null)
            return comercialTransferUser.getFullName();
        else
            return " ";
    }

    public String getReviewUser() {
        if(this.reviewUser != null)
            return reviewUser.getFullName();
        else
            return " ";
    }

    public String getDeclinedDateCase() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.declineDate != null) {

            String strDate = dateFormat.format(this.declineDate);
            return strDate;
        }
        else
            return " ";
    }

    public String getAcceptanceDateCase() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.acceptanceDate != null) {
            String strDate = dateFormat.format(this.acceptanceDate);
            return strDate;
        }
        else
            return " ";
    }
    public String getTechnicianTransferDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.technicianTransferDate != null){
            String strDate = dateFormat.format(this.technicianTransferDate);
            return strDate;
        }
        else
            return " ";
    }

    public String getComercialTransferDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.comercialTransferDate != null) {
            String strDate = dateFormat.format(this.comercialTransferDate);
            return strDate;
        }
        else
            return " ";
    }

    public String getReviewDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.reviewDate != null) {
            String strDate = dateFormat.format(this.reviewDate);
            return strDate;
        }
        else
            return " ";
    }
}
