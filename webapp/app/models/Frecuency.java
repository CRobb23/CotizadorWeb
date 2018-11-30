package models;

import objects.PaymentOption;
import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class Frecuency extends Model {
	
	@Column(length=25)
	public String name;
	
	public Integer code;
	
	@Column(length=25)
	public Integer frecuencyInYear;
	
	@Column(length=100)
	public String description;

    @OneToMany(mappedBy = "frecuency")
    public List<ER_Payment_Frecuency> paymentFrecuencies;
    
}
