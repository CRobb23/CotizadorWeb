package models;


import play.db.jpa.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class ER_ReviewDetail extends Model {

    public Date declineDate;

    public Date acceptanceDate;

    public Date technicianTransferDate;

    public Date comercialTransferDate;

    public Date reviewDate;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
    @JoinColumn(name="status_id", nullable=true)
    public ER_ReviewStatus status;



    public String getDeclinedDateCase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        if(this.declineDate != null) {

            String strDate = dateFormat.format(this.declineDate);
            return strDate;
        }
        else
            return "";
    }

    public String getAcceptanceDateCase() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        if(this.acceptanceDate != null) {
            String strDate = dateFormat.format(this.acceptanceDate);
            return strDate;
        }
        else
            return "";
    }
    public String getTechnicianTransferDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        if(this.technicianTransferDate != null){
            String strDate = dateFormat.format(this.technicianTransferDate);
            return strDate;
        }
        else
            return "";
    }

    public String getComercialTransferDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        if(this.comercialTransferDate != null) {
            String strDate = dateFormat.format(this.technicianTransferDate);
            return strDate;
        }
        else
            return "";
    }

    public String getReviewDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        if(this.reviewDate != null) {
            String strDate = dateFormat.format(this.reviewDate);
            return strDate;
        }
        else
            return "";
    }
}
