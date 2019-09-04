package models;


import play.db.jpa.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.text.ParseException;

@Entity
public class ER_ReviewDetail extends Model {

    public Date declineDate;

    public Integer timesReturned;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User declineUser;

    public Date acceptanceDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User acceptanceUser;

    public Date firstTechnicianTransferDate;

    public Date technicianTransferDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User tecchnicianTransferUser;

    public Date firstComercialTransferDate;

    public Date comercialTransferDate;

    @ManyToOne(fetch=FetchType.LAZY)
    public ER_User comercialTransferUser;

    public Date firstReviewDate;

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

    public String getFirstTechnicianTransferDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.firstTechnicianTransferDate != null){
            String strDate = dateFormat.format(this.firstTechnicianTransferDate);
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

    public String getFirstComercialTransferDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.firstComercialTransferDate != null){
            String strDate = dateFormat.format(this.firstComercialTransferDate);
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

    public String getFirstReviewDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(this.firstReviewDate != null){
            String strDate = dateFormat.format(this.firstReviewDate);
            return strDate;
        }
        else
            return " ";
    }
    public Integer getDaysBetweenRevisionAndCommercial() {
	try {
	Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFirstReviewDate());
	Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getComercialTransferDate());
        if(this.getFirstReviewDate() != "" && this.getComercialTransferDate()!= "" )
            return differenceBetweenDates(date1,date2 );
        else
            return 0;
	} catch (ParseException e){
		e.printStackTrace();
		return 0;
	}
    }

    public Integer getDaysBetweenCommercialAndTecnician() {
	try {
	Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFirstComercialTransferDate());
	Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getTechnicianTransferDate());

        if(this.getFirstComercialTransferDate() != "" && this.getTechnicianTransferDate() != "" )
            return differenceBetweenDates(date1,date2);
        else
            return 0;
	} catch (ParseException e){
		e.printStackTrace();
		return 0;
	}
    }

    public Integer getDaysBetweenTecnicianAndAccepted() {
	try{
	Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFirstTechnicianTransferDate());
	Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getAcceptanceDateCase());

        if(this.getFirstTechnicianTransferDate() != "" && this.getAcceptanceDateCase() != "")
            return differenceBetweenDates(date1,date2);
        else
            return 0;
	
	} catch (ParseException e){
		e.printStackTrace();
		return 0;
	}
    }
    public Integer getDaysBetweenTecnicianAndDeclined() {
	try{
	Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getFirstTechnicianTransferDate());
	Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(this.getDeclinedDateCase());

        if(this.getFirstTechnicianTransferDate() != "" && this.getDeclinedDateCase() != "" )
		return differenceBetweenDates(date1,date2);
        else
            return 0;
	} catch (ParseException e){
		e.printStackTrace();
		return 0;
	}
    }

    public Integer getTotalDaysBetweenStages() {
        return getDaysBetweenRevisionAndCommercial() + getDaysBetweenCommercialAndTecnician() + getDaysBetweenTecnicianAndAccepted() + getDaysBetweenTecnicianAndDeclined();
    }


    public static int differenceBetweenDates(Date fecha1, Date fecha2){
        long startTime = fecha1.getTime();
        long endTime = fecha2.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        return (int)diffDays;
    }
}
