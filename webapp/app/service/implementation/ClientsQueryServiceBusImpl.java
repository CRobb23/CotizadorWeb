package service.implementation;

import models.ws.QueryClientRequest;
import models.ws.QueryClientResponse;
import play.modules.guice.InjectSupport;
import service.ClientsQueryWebService;
import service.JsonService;

import javax.inject.Inject;

@InjectSupport
public class ClientsQueryServiceBusImpl extends ExternalJsonAbstractService implements ClientsQueryWebService {

    private QueryClientRequest clientQueryRequest;
    private QueryClientResponse clientQueryResponse;

    @Inject
    public ClientsQueryServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public QueryClientResponse clientQuery(QueryClientRequest criteria) {
        this.clientQueryRequest = criteria;
        callServiceBus();
        return clientQueryResponse;
    }

    @Override
    protected String getEndpoint() {
        return "/query/client";
    }

    @Override
    protected Object getReqObject() {
        return clientQueryRequest;
    }

    @Override
    protected void setResObject(Object object) {
        this.clientQueryResponse = (QueryClientResponse) object;
    }

    @Override
    protected Class getResClass() {
        return QueryClientResponse.class;
    }

}
