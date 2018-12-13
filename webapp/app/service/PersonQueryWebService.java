package service;

import models.ws.QueryPersonDetailRequest;
import models.ws.QueryPersonDetailResponse;
import models.ws.QueryVehicleRequest;
import models.ws.QueryVehicleResponse;

public interface PersonQueryWebService {
    QueryPersonDetailResponse queryPersonDetail(QueryPersonDetailRequest criteria);
}
