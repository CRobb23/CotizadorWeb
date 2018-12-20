package corpbi;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PolizasRoble", targetNamespace = "CorpBI")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PolizasRoble {


    /**
     * 
     * @param strXMLInput
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "Solicitud", action = "Solicitud_Poliza")
    @WebResult(name = "strXMLOutput", targetNamespace = "CorpBI")
    @RequestWrapper(localName = "Solicitud", targetNamespace = "CorpBI", className = "corpbi.Solicitud")
    @ResponseWrapper(localName = "SolicitudResponse", targetNamespace = "CorpBI", className = "corpbi.SolicitudResponse")
    String solicitud(
        @WebParam(name = "strXMLInput", targetNamespace = "CorpBI")
        String strXMLInput);

}