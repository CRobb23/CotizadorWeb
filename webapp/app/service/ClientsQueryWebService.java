package service;

import models.ws.QueryClientRequest;
import models.ws.QueryClientResponse;

public interface ClientsQueryWebService {
    QueryClientResponse clientQuery(QueryClientRequest criteria);
}
