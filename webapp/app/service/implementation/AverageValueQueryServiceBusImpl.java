package service.implementation;

import models.ws.QueryAverageValueVehicleRequest;
import models.ws.QueryAverageValueVehicleResponse;
import play.modules.guice.InjectSupport;
import service.AverageValueQueryWebService;
import service.JsonService;

import javax.inject.Inject;

@InjectSupport
public class AverageValueQueryServiceBusImpl extends ExternalJsonAbstractService implements AverageValueQueryWebService {

    private QueryAverageValueVehicleRequest averageValueQueryRequest;
    private QueryAverageValueVehicleResponse averageValueQueryResponse;

    @Inject
    public AverageValueQueryServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public QueryAverageValueVehicleResponse averageValueQuery(QueryAverageValueVehicleRequest criteria) {
        this.averageValueQueryRequest = criteria;
        callServiceBus();
        return averageValueQueryResponse;
    }

    @Override
    protected String getEndpoint() {
        return "/query/average";
    }

    @Override
    protected Object getReqObject() {
        return averageValueQueryRequest;
    }

    @Override
    protected void setResObject(Object object) {
        this.averageValueQueryResponse = (QueryAverageValueVehicleResponse) object;
    }

    @Override
    protected Class getResClass() {
        return QueryAverageValueVehicleResponse.class;
    }

}
