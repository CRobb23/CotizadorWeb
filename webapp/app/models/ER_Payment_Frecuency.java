package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Payment_Frecuency extends Model {
	
	public Integer numberOfPayments;
	
	@ManyToOne
	public Frecuency frecuency;
	
	@Required
	@Min(0)
	public BigDecimal fractioningFee;
    
}
