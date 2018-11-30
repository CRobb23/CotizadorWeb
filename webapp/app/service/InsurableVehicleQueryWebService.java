package service;

import models.ws.QueryVehicleRequest;
import models.ws.QueryVehicleResponse;

public interface InsurableVehicleQueryWebService {
    QueryVehicleResponse insurableVehicleQuery(QueryVehicleRequest criteria);
}
