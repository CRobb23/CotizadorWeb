package service.implementation;

import models.ws.PolicyProductRequest;
import models.ws.PolicyProductResponse;
import service.ProductPolicyWebService;
import play.modules.guice.InjectSupport;
import javax.inject.Inject;
import service.JsonService;

@InjectSupport
public class ProductPolicyWebServiceImpl  extends ExternalJsonAbstractService implements ProductPolicyWebService {

    private PolicyProductRequest policyProductRequest;
    private PolicyProductResponse policyProductResponse;

    @Inject
    public ProductPolicyWebServiceImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public PolicyProductResponse policyProductRequest (PolicyProductRequest criteria) {
        this.policyProductRequest = criteria;
        callServiceBus();
        return policyProductResponse;
    }

    @Override
    protected String getEndpoint() {
        return "/query/policyProduct";
    }

    @Override
    protected Object getReqObject() {
        return policyProductRequest;
    }

    @Override
    protected void setResObject(Object object) {
        this.policyProductResponse = (PolicyProductResponse) object;
    }

    @Override
    protected Class getResClass() {
        return PolicyProductResponse.class;
    }

}

