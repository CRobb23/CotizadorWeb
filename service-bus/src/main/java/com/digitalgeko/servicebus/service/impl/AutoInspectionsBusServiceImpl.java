package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.exceptions.ConnectionException;
import com.digitalgeko.servicebus.exceptions.ConvertException;
import com.digitalgeko.servicebus.model.rest.request.AutoInspectionCreateRestRequest;
import com.digitalgeko.servicebus.model.rest.request.AutoInspectionUpdateRestRequest;
import com.digitalgeko.servicebus.model.rest.response.AutoInspectionCreateRestResponse;
import com.digitalgeko.servicebus.model.rest.response.AutoInspectionUpdateRestResponse;
import com.digitalgeko.servicebus.model.soap.request.AutoInspectionCreateSoapRequest;
import com.digitalgeko.servicebus.model.soap.request.AutoInspectionUpdateSoapRequest;
import com.digitalgeko.servicebus.model.soap.response.AutoInspectionCreateSoapResponse;
import com.digitalgeko.servicebus.model.soap.response.AutoInspectionUpdateSoapResponse;
import com.digitalgeko.servicebus.outgoing.rest.AutoInspectionServiceRestOutbound;
import com.digitalgeko.servicebus.outgoing.soap.BrokerSoapOutbound;
import com.digitalgeko.servicebus.service.AutoInspectionsBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoInspectionsBusServiceImpl extends AbstractBusServiceImpl implements AutoInspectionsBus {

    @Autowired
    private AutoInspectionServiceRestOutbound autoInspectionServiceRestOutbound;
    @Autowired
    private BrokerSoapOutbound brokerSoapOutbound;

    @Override
    public String updateInspectionState(String soapMessage) {
        try {
            soapMessage = soapMessage.replace(AutoInspectionUpdateSoapRequest.RQ_CODE, "");
            String restMessage = fromSOAPtoJSON(soapMessage, AutoInspectionUpdateSoapRequest.class, AutoInspectionUpdateRestRequest.class);
            String restResponse = autoInspectionServiceRestOutbound.updateAutoInspectionState(restMessage);
            String soapResponse = fromJSONtoSOAP(restResponse, AutoInspectionUpdateRestResponse.class, AutoInspectionUpdateSoapResponse.class);
            soapResponse = AutoInspectionUpdateSoapRequest.RS_CODE + soapResponse;
            return soapResponse;
        } catch (ConvertException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String createAutoInspection(String restMessage) {
        try {
            String soapMessage = fromJSONtoSOAP(restMessage, AutoInspectionCreateRestRequest.class, AutoInspectionCreateSoapRequest.class);
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            String restResponse = fromJSONtoSOAP(soapResponse, AutoInspectionCreateSoapResponse.class, AutoInspectionCreateRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

}
