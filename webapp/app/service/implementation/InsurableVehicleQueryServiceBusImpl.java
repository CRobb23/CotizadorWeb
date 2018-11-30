package service.implementation;

import models.ws.QueryAverageValueVehicleRequest;
import models.ws.QueryAverageValueVehicleResponse;
import models.ws.QueryVehicleRequest;
import models.ws.QueryVehicleResponse;
import play.modules.guice.InjectSupport;
import service.AverageValueQueryWebService;
import service.InsurableVehicleQueryWebService;
import service.JsonService;

import javax.inject.Inject;

@InjectSupport
public class InsurableVehicleQueryServiceBusImpl extends ExternalJsonAbstractService implements InsurableVehicleQueryWebService {

    private QueryVehicleRequest request;
    private QueryVehicleResponse response;

    @Inject
    public InsurableVehicleQueryServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public QueryVehicleResponse insurableVehicleQuery(QueryVehicleRequest criteria) {
        this.request = criteria;
        callServiceBus();
        return response;
    }

    @Override
    protected String getEndpoint() {
        return "/query/insurable";
    }

    @Override
    protected Object getReqObject() {
        return request;
    }

    @Override
    protected void setResObject(Object object) {
        this.response = (QueryVehicleResponse) object;
    }

    @Override
    protected Class getResClass() {
        return QueryVehicleResponse.class;
    }

}
