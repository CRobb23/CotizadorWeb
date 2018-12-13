package service.implementation;

import models.ws.QueryPersonDetailRequest;
import models.ws.QueryPersonDetailResponse;
import play.modules.guice.InjectSupport;
import service.JsonService;
import service.PersonQueryWebService;

import javax.inject.Inject;

@InjectSupport
public class PersonQueryServiceBusImpl extends ExternalJsonAbstractService implements PersonQueryWebService {

    private QueryPersonDetailRequest request;
    private QueryPersonDetailResponse response;

    @Inject
    public PersonQueryServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public QueryPersonDetailResponse queryPersonDetail(QueryPersonDetailRequest criteria) {
        this.request = criteria;
        callServiceBus();
        return response;
    }

    @Override
    protected String getEndpoint() {
        return "/query/personDetail";
    }

    @Override
    protected Object getReqObject() {
        return request;
    }

    @Override
    protected void setResObject(Object object) {
        this.response = (QueryPersonDetailResponse) object;
    }

    @Override
    protected Class getResClass() {
        return QueryPersonDetailResponse.class;
    }

}
