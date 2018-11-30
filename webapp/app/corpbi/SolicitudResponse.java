package corpbi;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="strXMLOutput" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "strXMLOutput"
})
@XmlRootElement(name = "SolicitudResponse")
public class SolicitudResponse {

    @XmlElement(required = true)
    protected String strXMLOutput;

    /**
     * Gets the value of the strXMLOutput property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrXMLOutput() {
        return strXMLOutput;
    }

    /**
     * Sets the value of the strXMLOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrXMLOutput(String value) {
        this.strXMLOutput = value;
    }

}
