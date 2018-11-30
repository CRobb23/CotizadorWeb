package objects;

public class VehicleLine {

	@be.objectify.led.Property("MARCA")
	public String brand;
	
	@be.objectify.led.Property("LINEA")
	public String line;
	
	@be.objectify.led.Property("CLASE")
	public String vehicleClass;
	
	@be.objectify.led.Property("ASEGURABLE")
	public String insurable;
	
//	@be.objectify.led.Property("LOJACK")
//	public String requiresLoJack;
	
	@be.objectify.led.Property("ANIOLOJACK")
	public String loJackYear;
	
	@Override
	public String toString() {
		return "Brand: " + this.brand + ", Line: " + this.line + ", Class: " + this.vehicleClass;
	}
	
}
