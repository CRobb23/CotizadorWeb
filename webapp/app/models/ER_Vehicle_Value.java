package models;

import play.*;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Vehicle_Value extends Model {
	
	@Required
	@ManyToOne
	public ER_Year year;
	
	@Required
	@Column(precision=19, scale=2)
	@Min(0)
	public BigDecimal value;
	
	@Required
	@ManyToOne
	public ER_Vehicle_Line line;
    
}
