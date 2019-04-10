package com.digitalgeko.servicebus.incoming.soap;

import com.digitalgeko.servicebus.service.BdeoInspectionsBus;
import corpbi.BdeoInspectionRequest;
import corpbi.BdeoInspectionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class  BdeoInspectionServiceSoapInbound {

    private static final String NAMESPACE_URI = "CorpBI";

    protected static final Logger log = LoggerFactory.getLogger(BdeoInspectionServiceSoapInbound.class);

    @Autowired
    private BdeoInspectionsBus bdeoInspectionsBus;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "bdeoInspectionRequest")
    @ResponsePayload
    public BdeoInspectionResponse updateInspectionState(@RequestPayload BdeoInspectionRequest request) {
        log.info(request.getMessage());
        String responseMessage = bdeoInspectionsBus.parseBdeoMessage(request.getMessage());
        BdeoInspectionResponse response = new BdeoInspectionResponse();
        response.setMessage(responseMessage);
        return response;
    }
}
