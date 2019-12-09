package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.exceptions.ConnectionException;
import com.digitalgeko.servicebus.exceptions.ConvertException;
import com.digitalgeko.servicebus.model.rest.request.*;
import com.digitalgeko.servicebus.model.rest.response.*;
import com.digitalgeko.servicebus.model.rest.response.YoungCoverageInputRestResponse;
import com.digitalgeko.servicebus.model.soap.request.*;
import com.digitalgeko.servicebus.model.soap.response.*;
import com.digitalgeko.servicebus.outgoing.soap.BrokerSoapOutbound;
import com.digitalgeko.servicebus.service.PolicyInputBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyInputBusServiceImpl extends AbstractBusServiceImpl implements PolicyInputBus {

    @Autowired
    private BrokerSoapOutbound brokerSoapOutbound;

    @Override
    public String personClientInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, PersonClientInputRestRequest.class, PersonClientInputSoapRequest.class);
            soapMessage = PersonClientInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(PersonClientInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, PersonClientInputSoapResponse.class, PersonClientInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String businessClientInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, BusinessClientInputRestRequest.class, BusinessClientInputSoapRequest.class);
            soapMessage = BusinessClientInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(BusinessClientInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, BusinessClientInputSoapResponse.class, BusinessClientInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String payerDataInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, PayerInputRestRequest.class, PayerInputSoapRequest.class);
            soapMessage = PayerInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(PayerInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, PayerInputSoapResponse.class, PayerInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String policyDataInput(String message) {
        try {
            log.info("incoming >: " + message);
            String soapMessage = fromJSONtoSOAP(message, PolicyInputRestRequest.class, PolicyInputSoapRequest.class);
            soapMessage = PolicyInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(PolicyInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, PolicyInputSoapResponse.class, PolicyInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String vehicleDataInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, VehicleInputRestRequest.class, VehicleInputSoapRequest.class);
            soapMessage = VehicleInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(VehicleInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, VehicleInputSoapResponse.class, VehicleInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String coverageListInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, CoveragesListInputRestRequest.class, CoveragesListInputSoapRequest.class);
            soapMessage = CoveragesListInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(CoveragesListInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, CoveragesListInputSoapResponse.class, CoveragesListInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String primeListInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, PrimeInputRestRequest.class, PrimeInputSoapRequest.class);
            soapMessage = PrimeInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(PrimeInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, PrimeInputSoapResponse.class, PrimeInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String youngerCoveragesInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, YoungCoverageInputRestRequest.class, YoungCoverageInputSoapRequest.class);
            soapMessage = YoungCoverageInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(YoungCoverageInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, YoungCoverageInputSoapResponse.class, YoungCoverageInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String paymentMethodInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, PaymentInputRestRequest.class, PaymentInputSoapRequest.class);
            soapMessage = PaymentInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(PaymentInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, PaymentInputSoapResponse.class, PaymentInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    @Override
    public String workflowDataInput(String message) {
        try {
            String soapMessage = fromJSONtoSOAP(message, WorkFlowInputRestRequest.class, WorkFlowInputSoapRequest.class);
            soapMessage = WorkFlowInputSoapRequest.RQ_CODE + soapMessage;
            String soapResponse = brokerSoapOutbound.sendBrokerMessage(soapMessage);
            soapResponse = soapResponse.replaceFirst(WorkFlowInputSoapRequest.RS_CODE, "");
            String restResponse = fromSOAPtoJSON(soapResponse, WorkFlowInputSoapResponse.class, WorkFlowInputRestResponse.class);
            return restResponse;
        } catch (ConvertException | ConnectionException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }
}
