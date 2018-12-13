package service;

import models.ws.QueryAverageValueVehicleRequest;
import models.ws.QueryAverageValueVehicleResponse;
import models.ws.QueryClientRequest;
import models.ws.QueryClientResponse;

public interface AverageValueQueryWebService {
    QueryAverageValueVehicleResponse averageValueQuery(QueryAverageValueVehicleRequest criteria);
}
