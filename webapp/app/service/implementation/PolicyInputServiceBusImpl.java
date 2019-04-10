package service.implementation;

import models.enums.PolicyInputOperation;
import models.ws.*;
import play.modules.guice.InjectSupport;
import service.BusinessQueryWebService;
import service.JsonService;
import service.PolicyInputWebService;

import javax.inject.Inject;

@InjectSupport
public class PolicyInputServiceBusImpl extends ExternalJsonAbstractService implements PolicyInputWebService {

    private Object request;
    private Object response;
    private PolicyInputOperation operation;

    @Inject
    public PolicyInputServiceBusImpl(JsonService jsonService) {
        super(jsonService);
    }

    @Override
    public PersonClientResponse sendPersonClient(PersonClientRequest criteria) {
        this.operation = PolicyInputOperation.PERSON_INPUT;
        this.request = criteria;
        callServiceBus();
        return (PersonClientResponse) response;
    }

    @Override
    public BusinessClientResponse sendBusinessClient(BusinessClientRequest criteria) {
        this.operation = PolicyInputOperation.BUSINESS_INPUT;
        this.request = criteria;
        callServiceBus();
        return (BusinessClientResponse) response;
    }

    @Override
    public PayerResponse sendDataPayer(PayerRequest criteria) {
        this.operation = PolicyInputOperation.PAYER_INPUT;
        this.request = criteria;
        callServiceBus();
        return (PayerResponse) response;
    }

    @Override
    public PolicyResponse sendDataPolicy(PolicyRequest criteria) {
        this.operation = PolicyInputOperation.POLICY_INPUT;
        this.request = criteria;
        callServiceBus();
        return (PolicyResponse) response;
    }

    @Override
    public VehicleResponse sendDataVehicle(VehicleRequest criteria) {
        this.operation = PolicyInputOperation.VEHICLE_INPUT;
        this.request = criteria;
        callServiceBus();
        return (VehicleResponse) response;
    }

    @Override
    public CoveragesResponse sendListCoverages(CoveragesRequest criteria) {
        this.operation = PolicyInputOperation.COVERAGES_INPUT;
        this.request = criteria;
        callServiceBus();
        return (CoveragesResponse) response;
    }

    @Override
    public PrimeResponse sendPrimeList(PrimeRequest criteria) {
        this.operation = PolicyInputOperation.PRIME_INPUT;
        this.request = criteria;
        callServiceBus();
        return (PrimeResponse) response;
    }

    @Override
    public YoungerResponse sendCoveragesYounger(YoungerRequest criteria) {
        this.operation = PolicyInputOperation.YOUNG_INPUT;
        this.request = criteria;
        callServiceBus();
        return (YoungerResponse) response;
    }

    @Override
    public PaymentMethodResponse sendPaymentMethod(PaymentMethodRequest criteria) {
        this.operation = PolicyInputOperation.PAYMENT_INPUT;
        this.request = criteria;
        callServiceBus();
        return (PaymentMethodResponse) response;
    }

    @Override
    public WorkFlowResponse sendDataWorkFlow(WorkFlowRequest criteria) {
        this.operation = PolicyInputOperation.WORKFLOW_INPUT;
        this.request = criteria;
        callServiceBus();
        return (WorkFlowResponse) response;
    }

    @Override
    protected String getEndpoint() {
        switch (operation) {
            case PERSON_INPUT:
                return "/input/person";
            case BUSINESS_INPUT:
                return "/input/business";
            case PAYER_INPUT:
                return "/input/payer";
            case POLICY_INPUT:
                return "/input/policy";
            case VEHICLE_INPUT:
                return "/input/vehicle";
            case COVERAGES_INPUT:
                return "/input/coverage";
            case PRIME_INPUT:
                return "/input/prime";
            case YOUNG_INPUT:
                return "/input/young";
            case PAYMENT_INPUT:
                return "/input/payment";
            case WORKFLOW_INPUT:
                return "/input/workflow";
        }
        return "";
    }

    @Override
    protected Object getReqObject() {
        return request;
    }

    @Override
    protected void setResObject(Object object) {
        this.response = object;
    }

    @Override
    protected Class getResClass() {
        switch (operation) {
            case PERSON_INPUT:
                return PersonClientResponse.class;
            case BUSINESS_INPUT:
                return BusinessClientResponse.class;
            case PAYER_INPUT:
                return PayerResponse.class;
            case POLICY_INPUT:
                return PolicyResponse.class;
            case VEHICLE_INPUT:
                return VehicleResponse.class;
            case COVERAGES_INPUT:
                return CoveragesResponse.class;
            case PRIME_INPUT:
                return PrimeResponse.class;
            case YOUNG_INPUT:
                return YoungerResponse.class;
            case PAYMENT_INPUT:
                return PaymentMethodResponse.class;
            case WORKFLOW_INPUT:
                return WorkFlowResponse.class;
        }
        return null;
    }

}
