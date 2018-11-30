package objects;

import be.objectify.led.*;

public class VehicleValue {
	
	@be.objectify.led.Property("MARCA")
	public String brand;
	
	@be.objectify.led.Property("LINEA")
	public String line;
	
	@be.objectify.led.Property("ANIO")
	public String year;
	
	@be.objectify.led.Property("VALOR")
	public String value;
	
	@Override
	public String toString() {
		return "Brand: " + this.brand + ", Line: " + this.line + ", Year: " + this.year + ", Value: " + this.value;
	}

}
