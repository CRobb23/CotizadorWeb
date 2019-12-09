package service;

import models.ws.PolicyProductRequest;
import models.ws.PolicyProductResponse;

public interface ProductPolicyWebService {
    PolicyProductResponse policyProductRequest (PolicyProductRequest criteria);
}
