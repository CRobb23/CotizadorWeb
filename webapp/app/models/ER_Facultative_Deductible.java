package models;

import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ER_Facultative_Deductible extends Model {

	// byron guerrero - 12/1/16 - se cambio el maximo a 1000 para que puedar recibir un incremento del 1000 %
	// el maximo anterior era 200.

	@Required
	@Min(0)
	@Max(1000)
	public BigDecimal deductibleIncrease;
	
	@Required
	@Min(0)
	@Max(100)
	//byron guerrero - 20/1/16 - se cambio el max de 50 a 100 por requerimiento de Sthefany Descyre de el Roble

	public BigDecimal primeDiscount;
}
