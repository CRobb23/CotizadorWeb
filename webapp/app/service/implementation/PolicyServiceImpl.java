package service.implementation;

import models.ws.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.modules.guice.InjectSupport;
import service.PolicyService;

@InjectSupport
public class PolicyServiceImpl implements PolicyService{
	
	private final Logger LOG = LoggerFactory.getLogger(PolicyService.class);
	private final String CONNECTION_ERROR = "Ha ocurrido un error en la conexión.";

	public QueryClientResponse queryClient(QueryClientRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            LOG.info("queryClient SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());

//            String response = "701<consultaCliente><msgRespuesta>SATISFACTORIO</msgRespuesta><listaClientes><cliente><nit>123456</nit><dpi>123456789</dpi><passaporte>987654321</passaporte><nombreCliente>GUSTAVO GOMEZ</nombreCliente><cifCliente>111222</cifCliente><codigoCliente>321</codigoCliente></cliente><cliente><nit>123456</nit><dpi>123456789</dpi><passaporte>654798321</passaporte><nombreCliente>GUSTAVO ADOLFO GOMEZ</nombreCliente><cifCliente>222333</cifCliente><codigoCliente>322</codigoCliente></cliente></listaClientes></consultaCliente>";

            LOG.info("queryClient SOAP RESPONSE:\n\n" + response + "\n");

            MessageHolder.printData(null);
            int responseIndex = response.indexOf(QueryClientResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (QueryClientResponse) XmlMarshalUtil.fromXml(QueryClientResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new QueryClientResponse(CONNECTION_ERROR);*/
        return null;
    }

	public QueryAverageValueVehicleResponse queryAverageValueVehicle(QueryAverageValueVehicleRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            LOG.info("queryAverageValueVehicle SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());

//            String response = "706<consultaValorPromedioVehiculo><msgRespuesta>00000- SATISFACTORIO</msgRespuesta><valorPromedio>142910.00</valorPromedio><cantidadVehiculos>3</cantidadVehiculos></consultaValorPromedioVehiculo>";
            LOG.info("queryAverageValueVehicle SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(QueryAverageValueVehicleResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (QueryAverageValueVehicleResponse) XmlMarshalUtil.fromXml(QueryAverageValueVehicleResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new QueryAverageValueVehicleResponse(CONNECTION_ERROR);*/
        return null;
    }
	
	public QueryVehicleResponse queryInsurableVehicle(QueryVehicleRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("queryInsurableVehicle SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());

//            String response = "711<consultaVehiculo><msgRespuesta>00000-SATISFACTORIO **Vehiculo Asegurable**</msgRespuesta></consultaVehiculo>";
            
            LOG.info("queryInsurableVehicle SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(QueryVehicleResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (QueryVehicleResponse) XmlMarshalUtil.fromXml(QueryVehicleResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        
        return new QueryVehicleResponse(CONNECTION_ERROR);*/
        return null;
    }
	
    public PersonClientResponse sendPersonClient(PersonClientRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendPersonClient SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendPersonClient SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(PersonClientResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (PersonClientResponse) XmlMarshalUtil.fromXml(PersonClientResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new PersonClientResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public BusinessClientResponse sendBusinessClient(BusinessClientRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendBusinessClient SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendBusinessClient SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(BusinessClientResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (BusinessClientResponse) XmlMarshalUtil.fromXml(BusinessClientResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new BusinessClientResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public PayerResponse sendDataPayer(PayerRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendDataPayer SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendDataPayer SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(PayerResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (PayerResponse) XmlMarshalUtil.fromXml(PayerResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new PayerResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public PolicyResponse sendDataPolicy(PolicyRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendDataPolicy SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendDataPolicy SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(PolicyResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (PolicyResponse) XmlMarshalUtil.fromXml(PolicyResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new PolicyResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public VehicleResponse sendDataVehicle(VehicleRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendDataVehicle SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendDataVehicle SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(VehicleResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (VehicleResponse) XmlMarshalUtil.fromXml(VehicleResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new VehicleResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public CoveragesResponse sendListCoverages(CoveragesRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendListCoverages SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendListCoverages SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(CoveragesResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (CoveragesResponse) XmlMarshalUtil.fromXml(CoveragesResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new CoveragesResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public PrimeResponse sendPrimeList(PrimeRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendListPrima SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendListPrima SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(PrimeResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (PrimeResponse) XmlMarshalUtil.fromXml(PrimeResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new PrimeResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public YoungerResponse sendCoveragesYounger(YoungerRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendCoveragesYounger SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendCoveragesYounger SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(YoungerResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (YoungerResponse) XmlMarshalUtil.fromXml(YoungerResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new YoungerResponse(CONNECTION_ERROR);*/
        return null;
    }
   
    public PaymentMethodResponse sendPaymentMethod(PaymentMethodRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendPaymentMethod SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendPaymentMethod SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(PaymentMethodResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (PaymentMethodResponse) XmlMarshalUtil.fromXml(PaymentMethodResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new PaymentMethodResponse(CONNECTION_ERROR);*/
        return null;
    }
    
    public WorkFlowResponse sendDataWorkFlow(WorkFlowRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);
            
            LOG.info("sendDataWorkFlow SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());
            
            LOG.info("sendPaymentMethod SOAP RESPONSE:\n\n" + response + "\n");
            
            MessageHolder.printData(null);
            int responseIndex = response.indexOf(WorkFlowResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (WorkFlowResponse) XmlMarshalUtil.fromXml(WorkFlowResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new WorkFlowResponse(CONNECTION_ERROR);*/
        return null;
    }

    public QueryPersonDetailResponse queryPersonDetail(QueryPersonDetailRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);

            LOG.info("queryClient SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());

//            String response = "776<datosClientePersonal><codigoCliente>75389</codigoCliente><nit>3913920-4</nit><titulo>G45</titulo><primerNombre>LILIAN</primerNombre><segundoNombre>MICHELLE</segundoNombre><primerApellido>MINERA </primerApellido><segundoApellido>PICHE</segundoApellido><apellidoCasada></apellidoCasada><fechaNacimiento>16/05/2018</fechaNacimiento><sexo>2</sexo><edad>0</edad><profesion>A21</profesion><dpi>2485966020101</dpi><codigoCifBanco></codigoCifBanco><pasaporte></pasaporte><estadoCivil>1</estadoCivil><nacionalidad>1</nacionalidad><tipoLicencia>1</tipoLicencia><numeroLicencia>2485966020101</numeroLicencia><email>sdminera@elroble.com</email><direcciones><direccionCasa><direccionC>15 CALLE 32-81 COLONIA VENEZUELA</direccionC><paisC>001</paisC><departamentoC>GT 1 </departamentoC><municipioC>GT-1--101</municipioC><zonaC>GT-1--101009</zonaC><telefono1>78451245</telefono1><telefono2>96321458</telefono2><telefono3></telefono3></direccionCasa><direccionTrabajo><direccionT>7A AVENIDA 5-10 CENTRO FINANCIERO TORRE II NIVEL 16</direccionT><paisT>001</paisT><departamentoT>GT 1 </departamentoT><municipioT>GT-1--101</municipioT><zonaT>GT-1--101017</zonaT><telefonoT1>24203333</telefonoT1><telefonoT2></telefonoT2><telefonoT3></telefonoT3></direccionTrabajo></direcciones></datosClientePersonal>";

            LOG.info("queryClient SOAP RESPONSE:\n\n" + response + "\n");

            MessageHolder.printData(null);
            int responseIndex = response.indexOf(QueryPersonDetailResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (QueryPersonDetailResponse) XmlMarshalUtil.fromXml(QueryPersonDetailResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new QueryPersonDetailResponse(CONNECTION_ERROR);*/
        return null;
    }

    public QueryBusinessDetailResponse queryBusinessDetail(QueryBusinessDetailRequest criteria) {
        /*PolizasRoble service = null;
	    try{
            String request = XmlMarshalUtil.toXml(criteria.getClass(), criteria);
            request = criteria.TRANSACTION + StringUtil.prepareXmlRequest(request);

            LOG.info("queryClient SOAP REQUEST:\n\n" + request + "\n");

            service = WSClientFactory.getService(PolizasRoble_Service.class, PolizasRoble.class);
            String response = service.solicitud(request);
            response = StringUtil.prepareXmlResponse(response.trim());

//            String response = "781<datosClienteEmpresarial><nitEmpresa>C/F</nitEmpresa><tipoSociedad>AN</tipoSociedad><nombreEmpresa>LOS PATITOS S.A.</nombreEmpresa><razonSocial>LOS PATITOS S.A.</razonSocial><actividadEconomica>112</actividadEconomica><nacionalidadEmpresa>1</nacionalidadEmpresa><dpiEmpresa>271523022315155555</dpiEmpresa><proveedorEstado>N</proveedorEstado><emailEmpresa>kenny.hdr@gmail.com</emailEmpresa><numeroEscritura>1231564</numeroEscritura><fechaEscritura>25/06/2009</fechaEscritura><fechaConstitucion>04/06/2009</fechaConstitucion><codigoCifBanco></codigoCifBanco><datosRepresentanteLegal><primerNombreRep>CARMEN</primerNombreRep><segundoNombreRep></segundoNombreRep><primerApellidoRep>SAENZ</primerApellidoRep><segundoApellidoRep></segundoApellidoRep><apellidoCasadaRep></apellidoCasadaRep><nacionalidadRep>1</nacionalidadRep><registroRep>123456</registroRep><expedienteRep>78910</expedienteRep><extendidaEn>GUATEMALA</extendidaEn><fechaInscripcion>03/05/2018</fechaInscripcion><fechaNacimiento>06/07/1995</fechaNacimiento><profesionRep>A01</profesionRep><libro>1562</libro><folio>156</folio><dpiRep>2727272727272272727</dpiRep><nitRep>17926769</nitRep><pasaporteRep></pasaporteRep><sexoRep>1</sexoRep><estadoCivilRep>1</estadoCivilRep><emailRep>KENNY.HDR@GMAIL.COM</emailRep></datosRepresentanteLegal><direcciones><direccionEmpresa><direccionE>8 AVENIDA 6 CALLE </direccionE><paisE>001</paisE><departamentoE>GT 13</departamentoE><municipioE>GT-13-324</municipioE><zonaE>GT-13-324001</zonaE><telefono1>24562020</telefono1><telefono2>41416439</telefono2><telefono3></telefono3></direccionEmpresa><direccionRepresentanteLegal><direccionR>11 CALLE 1 -80</direccionR><paisR>001</paisR><departamentoR>GT 1 </departamentoR><municipioR>GT-1--105</municipioR><zonaR>GT-1--105005</zonaR><telefono1R>20202020</telefono1R><telefono2R></telefono2R><telefono3R></telefono3R></direccionRepresentanteLegal></direcciones></datosClienteEmpresarial>";

            LOG.info("queryClient SOAP RESPONSE:\n\n" + response + "\n");

            MessageHolder.printData(null);
            int responseIndex = response.indexOf(QueryBusinessDetailResponse.TRANSACTION.toString());
            if(responseIndex >= 0){
                response = response.substring(responseIndex+3);
                return (QueryBusinessDetailResponse) XmlMarshalUtil.fromXml(QueryBusinessDetailResponse.class, response);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            WSClientFactory.closeService(PolizasRoble_Service.class, service);
        }
        return new QueryBusinessDetailResponse(CONNECTION_ERROR);*/
        return null;
    }

}
