package service.implementation;

import models.ws.QueryBusinessDetailRequest;
import models.ws.QueryBusinessDetailResponse;
import models.ws.QueryPersonDetailRequest;
import models.ws.QueryPersonDetailResponse;
import play.modules.guice.InjectSupport;
import service.BusinessQueryWebService;
import service.JsonService;
import service.PersonQueryWebService;

import javax.inject.Inject;

@InjectSupport
public class BusinessQueryServiceBusImpl extends ExternalJsonAbstractService implements BusinessQueryWebService {

    private QueryBusinessDetailRequest request;
    private QueryBusinessDetailResponse response;

    @Inject
    public BusinessQueryServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public QueryBusinessDetailResponse queryBusinessDetail(QueryBusinessDetailRequest criteria) {
        this.request = criteria;
        callServiceBus();
        return response;
    }

    @Override
    protected String getEndpoint() {
        return "/query/businessDetail";
    }

    @Override
    protected Object getReqObject() {
        return request;
    }

    @Override
    protected void setResObject(Object object) {
        this.response = (QueryBusinessDetailResponse) object;
    }

    @Override
    protected Class getResClass() {
        return QueryBusinessDetailResponse.class;
    }

}
