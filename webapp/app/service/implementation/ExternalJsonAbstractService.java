package service.implementation;

import play.Logger;
import play.Play;
import play.libs.WS;
import service.JsonService;
import utils.StringConstants;

import javax.inject.Inject;

import static utils.StringUtil.escapeXml;
import static utils.StringUtil.escapeXmlResponse;

public abstract class ExternalJsonAbstractService {

    protected JsonService jsonService;

    @Inject
    public ExternalJsonAbstractService(JsonService jsonService) {
        this.jsonService = jsonService;
    }

    private String baseURL = Play.configuration.getProperty("serviceBus.baseURL");

    private WS.WSRequest prepareWS(String url) {
        return WS.url(url).setHeader(StringConstants.CONTENT_HEADER, StringConstants.JSON_CONTENT_HEADER);
    }

    protected void callServiceBus() {
        String url = getBaseURL() + getEndpoint();
        WS.WSRequest request = prepareWS(url);
        if (getReqObject() != null) {
            String requestStr = jsonService.toJson(getReqObject());
            requestStr = escapeXml(requestStr);


            Logger.info("SERVICE BUS REQUEST: " + requestStr);
            request.body(requestStr);
        } else {
            Logger.info("SERVICE BUS REQUEST EMPTY");
        }

        WS.HttpResponse response = request.post();
        String resStr = response.getString();

        resStr = escapeXmlResponse(resStr);
        Logger.info("SERVICE BUS RESPONSE: " + resStr);
        if (getResClass() != null) {
            setResObject(jsonService.getAsJson(resStr, getResClass()));
        }
    }

    protected String getBaseURL() {
        return baseURL;
    }

    abstract protected String getEndpoint();
    abstract protected Object getReqObject();
    abstract protected void setResObject(Object object);
    abstract protected Class getResClass();
}
