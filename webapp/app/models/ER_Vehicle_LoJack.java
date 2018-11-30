package models;

import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class ER_Vehicle_LoJack extends Model {
	
	@Required
	public Integer yearInit;

    @Required
    public Integer yearEnd;

    @Required
	@Min(0)
	public BigDecimal withLoJack;

    @Required
    @Min(0)
    public BigDecimal withoutLoJack;

    @Required
    @Min(0)
    public BigDecimal withoutLoJackPlus10;

    @Required
    @Min(0)
    public BigDecimal withoutLoJackPlus15;

    @Required
    @Min(0)
    public BigDecimal withoutLoJackPlusHalf;

    @Required
    @Min(0)
    public BigDecimal withoutLoJackPlusFull;

    @Required
	@ManyToOne
	public ER_Vehicle_Line line;
    
}
