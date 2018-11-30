package objects;

import be.objectify.led.Property;

public class VehicleLoJack {

	@Property("MARCA")
	public String brand;

	@Property("LINEA")
	public String line;

	@Property("ANIO_INICIO")
	public String yearInit;

    @Property("ANIO_FIN")
    public String yearEnd;

    @Property("DISPOSTIVO")
    public String withLoJack;

    @Property("SIN_DISPOSTIVO")
    public String withoutLoJack;

    @Property("MAS_DIEZ")
    public String withoutLoJackPlus10;

    @Property("MAS_QUINCE")
    public String withoutLoJackPlus15;

    @Property("MAS_500")
    public String withoutLoJackPlusHalf;

    @Property("MAS_850")
    public String withoutLoJackPlusFull;


}
