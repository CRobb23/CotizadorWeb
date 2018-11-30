package com.digitalgeko.servicebus.outgoing.soap;

import com.digitalgeko.servicebus.exceptions.ConnectionException;
import corpbiout.Solicitud;
import corpbiout.SolicitudResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.http.ClientHttpRequestMessageSender;

@Service
public class BrokerSoapOutbound extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(BrokerSoapOutbound.class);

    private String defaultUri;

    @Autowired
    public BrokerSoapOutbound(@Value("${my.ws.readtimeout}") String readTimeout, @Value("${my.ws.connectiontimeout}") String connectionTimeout,
                              @Value("${my.ws.defaulturi}") String defaultUri) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Integer.parseInt(connectionTimeout));
        requestFactory.setReadTimeout(Integer.parseInt(readTimeout));
        setMessageSender(new ClientHttpRequestMessageSender(requestFactory));
        this.defaultUri = defaultUri;
    }

    public String sendBrokerMessage(String message) throws ConnectionException {
        try {
            log.info("REQUEST: " + message);
            long initTime = System.currentTimeMillis();
            Solicitud request = new Solicitud();
            request.setStrXMLInput(message);
            getWebServiceTemplate().setDefaultUri(defaultUri);
            getWebServiceTemplate().setMarshaller(marshaller());
            getWebServiceTemplate().setUnmarshaller(marshaller());
            SolicitudResponse response = (SolicitudResponse) getWebServiceTemplate().marshalSendAndReceive(request);
            String respMessage = response.getStrXMLOutput();
            log.info("RESPONSE: " + respMessage);
            long endTime = System.currentTimeMillis();
            log.info("TIME (MS): " + (endTime - initTime));
            return respMessage;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ConnectionException("ERROR DE CONEXION HACIA AS/400");
        }
    }

    private Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // this is the package name specified in the <generatePackage> specified in
        // pom.xml
        marshaller.setContextPath("corpbiout");
        return marshaller;
    }
}
