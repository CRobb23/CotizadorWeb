package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.exceptions.ConvertException;
import com.digitalgeko.servicebus.model.rest.request.AutoInspectionCreateRestRequest;
import com.digitalgeko.servicebus.model.rest.request.AutoInspectionUpdateRestRequest;
import com.digitalgeko.servicebus.model.rest.request.LoginRestRequest;
import com.digitalgeko.servicebus.model.rest.response.AutoInspectionRestResponse;
import com.digitalgeko.servicebus.model.rest.response.LoginRestResponse;
import com.digitalgeko.servicebus.model.soap.request.AutoInspectionCreateSoapRequest;
import com.digitalgeko.servicebus.model.soap.request.AutoInspectionDeleteSoapRequest;
import com.digitalgeko.servicebus.model.soap.request.AutoInspectionQuerySoapRequest;
import com.digitalgeko.servicebus.model.soap.request.AutoInspectionUpdateSoapRequest;
import com.digitalgeko.servicebus.model.soap.response.AutoInspectionCreateSoapResponse;
import com.digitalgeko.servicebus.model.soap.response.AutoInspectionDeleteSoapResponse;
import com.digitalgeko.servicebus.model.soap.response.AutoInspectionQuerySoapResponse;
import com.digitalgeko.servicebus.model.soap.response.AutoInspectionUpdateSoapResponse;
import com.digitalgeko.servicebus.outgoing.rest.BdeoInspectionServiceRestOutbound;
import com.digitalgeko.servicebus.service.BdeoInspectionsBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class BdeoInspectionsBusServiceImpl extends AbstractBusServiceImpl implements BdeoInspectionsBus {

    private String bdeoUser;
    private String bdeoPass;
    private BdeoInspectionServiceRestOutbound bdeoServiceRestOutbound;
    private MultimediaServiceImpl multimediaService;

    @Autowired
    public BdeoInspectionsBusServiceImpl(BdeoInspectionServiceRestOutbound outboundService, MultimediaServiceImpl multimediaService,
                                         @Value("${bdeo.ws.user}") String bdeoUser, @Value("${bdeo.ws.pass}") String bdeoPass) {
        this.bdeoServiceRestOutbound = outboundService;
        this.multimediaService = multimediaService;
        this.bdeoUser = bdeoUser;
        this.bdeoPass = bdeoPass;
    }

    @Override
    public String parseBdeoMessage(String soapMessage) {
        String response = "";
        String requestCode = getSoapRequestCode(soapMessage);
        String message = getSoapMessage(soapMessage);
        switch (requestCode) {
            case AutoInspectionCreateSoapRequest.RQ_CODE:
                response = createAutoInspection(message);
                response = AutoInspectionCreateSoapRequest.RS_CODE + response;
                break;
            case AutoInspectionQuerySoapRequest.RQ_CODE:
                response = getAutoInspection(message);
                response = AutoInspectionQuerySoapRequest.RS_CODE + response;
                break;
            case AutoInspectionUpdateSoapRequest.RQ_CODE:
                response = updateAutoInspection(message);
                response = AutoInspectionUpdateSoapRequest.RS_CODE + response;
                break;
            case AutoInspectionDeleteSoapRequest.RQ_CODE:
                response = deleteAutoInspection(message);
                response = AutoInspectionDeleteSoapRequest.RS_CODE + response;
                break;
        }
        return response;
    }

    public String createAutoInspection(String soapMessage) {
        try {
            // Convert SOAP to JSON, call BDEO, return message
            String restMessage = fromSOAPtoJSON(soapMessage, AutoInspectionCreateSoapRequest.class, AutoInspectionCreateRestRequest.class);
            String restResponse = bdeoServiceRestOutbound.createAutoInspection(login(), restMessage);
            String soapResponse = fromJSONtoSOAP(restResponse, AutoInspectionRestResponse.class, AutoInspectionCreateSoapResponse.class);
            return soapResponse;
        } catch (ConvertException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public String deleteAutoInspection(String soapMessage) {
        try {
            // Convert SOAP to JSON, call BDEO, return message
            AutoInspectionDeleteSoapRequest soapRequest = (AutoInspectionDeleteSoapRequest) fromSOAP(soapMessage, AutoInspectionDeleteSoapRequest.class);
            bdeoServiceRestOutbound.deleteAutoInspection(login(), soapRequest.getId());
            String soapResponse = toSOAP(new AutoInspectionDeleteSoapResponse("SATISFACTORIO"));
            return soapResponse;
        } catch (ConvertException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public String getAutoInspection(String soapMessage) {
        try {
            // Convert SOAP to JSON, call BDEO
            AutoInspectionQuerySoapRequest soapRequest = (AutoInspectionQuerySoapRequest) fromSOAP(soapMessage, AutoInspectionQuerySoapRequest.class);
            String restResponse = bdeoServiceRestOutbound.queryAutoInspection(login(), soapRequest.getId());
            // Check for Status REVISADO, Sent Multimedia to Drive
            AutoInspectionRestResponse restObj = (AutoInspectionRestResponse) fromJSON(restResponse, AutoInspectionRestResponse.class);
            if (restObj.getStatus() == 2 || restObj.getStatus() == 4 || restObj.getStatus() == 5) {
                for (AutoInspectionRestResponse.InspectionImage image : restObj.getImages()) {
                    CompletableFuture<Boolean> front = multimediaService.processImage(restObj.getCaseId(), "frontImage.jpg", image.getFront());
                    CompletableFuture<Boolean> rear = multimediaService.processImage(restObj.getCaseId(), "rearImage.jpg", image.getRear());
                    CompletableFuture<Boolean> frontRight = multimediaService.processImage(restObj.getCaseId(), "frontRightImage.jpg", image.getFrontRight());
                    CompletableFuture<Boolean> rearRight = multimediaService.processImage(restObj.getCaseId(), "rearRightImage.jpg", image.getRearRight());
                    CompletableFuture<Boolean> right = multimediaService.processImage(restObj.getCaseId(), "rightImage.jpg", image.getRight());
                    CompletableFuture<Boolean> board = multimediaService.processImage(restObj.getCaseId(), "boardImage.jpg", image.getBoard());
                    CompletableFuture<Boolean> interiorBoard = multimediaService.processImage(restObj.getCaseId(), "interiorBoardImage.jpg", image.getInteriorBoard());
                    CompletableFuture<Boolean> trunk = multimediaService.processImage(restObj.getCaseId(), "trunkImage.jpg", image.getTrunk());
                    CompletableFuture<Boolean> tyre = multimediaService.processImage(restObj.getCaseId(), "tyreImage.jpg", image.getTyre());
                    CompletableFuture<Boolean> trunkTyre = multimediaService.processImage(restObj.getCaseId(), "trunkTyreImage.jpg", image.getTrunkTyre());
                    CompletableFuture<Boolean> hood = multimediaService.processImage(restObj.getCaseId(), "hoodImage.jpg", image.getHood());
                    CompletableFuture<Boolean> radioAir = multimediaService.processImage(restObj.getCaseId(), "radioImage.jpg", image.getRadioAir());
                    CompletableFuture<Boolean> windowSwitch = multimediaService.processImage(restObj.getCaseId(), "windowSwitchImage.jpg", image.getWindowSwitch());
                    CompletableFuture<Boolean> registration = multimediaService.processImage(restObj.getCaseId(), "registrarionImage.jpg", image.getRegistration());
                    CompletableFuture<Boolean> license = multimediaService.processImage(restObj.getCaseId(), "licenseImage.jpg", image.getLicense());
                    CompletableFuture<Boolean> dpi = multimediaService.processImage(restObj.getCaseId(), "dpiImage.jpg", image.getDpi());
                    CompletableFuture<Boolean> car = multimediaService.processImage(restObj.getCaseId(), "catImage.jpg", image.getCar());
                    CompletableFuture<Boolean> signature = multimediaService.processImage(restObj.getCaseId(), "signatureImage.jpg", image.getSignature());

                    CompletableFuture.allOf(front,rear,frontRight,rearRight,right,board,interiorBoard,trunk,
                            tyre,trunkTyre,hood,radioAir,windowSwitch,registration,license,dpi,car,signature).join();
                }
            }
            // Return message
            String soapResponse = fromJSONtoSOAP(restResponse, AutoInspectionRestResponse.class, AutoInspectionQuerySoapResponse.class);
            return soapResponse;
        } catch (ConvertException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public String updateAutoInspection(String soapMessage) {
        try {
            // Convert SOAP to JSON, call BDEO, return message
            AutoInspectionUpdateSoapRequest soapRequest = (AutoInspectionUpdateSoapRequest) fromSOAP(soapMessage, AutoInspectionUpdateSoapRequest.class);
            String restMessage = fromSOAPtoJSON(soapMessage, AutoInspectionUpdateSoapRequest.class, AutoInspectionUpdateRestRequest.class);
            bdeoServiceRestOutbound.updateAutoInspection(login(), soapRequest.getId(), restMessage);
            String soapResponse = toSOAP(new AutoInspectionUpdateSoapResponse("SATISFACTORIO"));
            return soapResponse;
        } catch (ConvertException e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    private String login() throws ConvertException {
        LoginRestRequest restRequest = new LoginRestRequest(bdeoUser, bdeoPass);
        String message = toJSON(restRequest);
        String response = bdeoServiceRestOutbound.login(message);
        LoginRestResponse restResponse = (LoginRestResponse) fromJSON(response, LoginRestResponse.class);
        return restResponse.getAccessToken();
    }

    private String getSoapRequestCode(String soapMessage) {
        if (soapMessage != null && soapMessage.length() > 3) {
            return soapMessage.substring(0, 3);
        }
        return "";
    }

    private String getSoapMessage(String soapMessage) {
        if (soapMessage != null && soapMessage.length() > 3) {
            return soapMessage.substring(3);
        }
        return "";
    }

}
