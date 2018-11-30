package service;

import models.ws.QueryBusinessDetailRequest;
import models.ws.QueryBusinessDetailResponse;

public interface BusinessQueryWebService {
    QueryBusinessDetailResponse queryBusinessDetail(QueryBusinessDetailRequest criteria);
}
