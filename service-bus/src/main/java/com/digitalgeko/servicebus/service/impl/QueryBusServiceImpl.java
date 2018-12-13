package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.exceptions.ConnectionException;
import com.digitalgeko.servicebus.exceptions.ConvertException;
import com.digitalgeko.servicebus.model.rest.request.*;
import com.digitalgeko.servicebus.model.rest.response.*;
import com.digitalgeko.servicebus.model.soap.request.*;
import com.digitalgeko.servicebus.model.soap.response.*;
import com.digitalgeko.servicebus.outgoing.soap.BrokerSoapOutbound;
import com.digitalgeko.servicebus.service.QueryBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueryBusServiceImpl extends AbstractBusServiceImpl implements QueryBus {

    @Autowired
    private BrokerSoapOutbound brokerSoapOutbound;

    @Override
    public String clientQuery(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, ClientQueryRestRequest.class, ClientQuerySoapRequest.class);
            soapMessage = ClientQuerySoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replace(ClientQuerySoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, ClientQuerySoapResponse.class, ClientQueryRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String averageValueQuery(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, AverageValueQueryRestRequest.class, AverageValueQuerySoapRequest.class);
            soapMessage = AverageValueQuerySoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replace(AverageValueQuerySoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, AverageValueQuerySoapResponse.class, AverageValueQueryRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String insurableVehicleQuery(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, InsurableVehicleQueryRestRequest.class, InsurableVehicleQuerySoapRequest.class);
            soapMessage = InsurableVehicleQuerySoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replace(InsurableVehicleQuerySoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, InsurableVehicleQuerySoapResponse.class, InsurableVehicleQueryRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String personDetailQuery(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, PersonDetailQueryRestRequest.class, PersonDetailQuerySoapRequest.class);
            soapMessage = PersonDetailQuerySoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replace(PersonDetailQuerySoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, PersonDetailQuerySoapResponse.class, PersonDetailQueryRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String businessDetailQuery(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, BusinessDetailQueryRestRequest.class, BusinessDetailQuerySoapRequest.class);
            soapMessage = BusinessDetailQuerySoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replace(BusinessDetailQuerySoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, BusinessDetailQuerySoapResponse.class, BusinessDetailQueryRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

}
