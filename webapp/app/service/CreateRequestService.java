package service;

import models.ER_Incident;
import models.ws.BusinessClientRequest;
import models.ws.CoveragesRequest;
import models.ws.PayerRequest;
import models.ws.PaymentMethodRequest;
import models.ws.PersonClientRequest;
import models.ws.PolicyRequest;
import models.ws.PolicyResponse;
import models.ws.PrimeRequest;
import models.ws.QueryAverageValueVehicleRequest;
import models.ws.QueryClientRequest;
import models.ws.QueryVehicleRequest;
import models.ws.VehicleRequest;
import models.ws.WorkFlowRequest;
import models.ws.YoungerRequest;

public interface CreateRequestService {

	QueryClientRequest createQueryRequest(ER_Incident incident);
    
    QueryAverageValueVehicleRequest createQueryAverageValueVehicleRequest(ER_Incident incident);
    
    QueryVehicleRequest createQueryVehicleRequest(ER_Incident incident);
    
    PersonClientRequest createPersonClientRequest(ER_Incident incident);
    
    BusinessClientRequest createBusinessClientRequest(ER_Incident incident);
    
    PayerRequest createPayerRequest(ER_Incident incident);
    
    PolicyRequest createPolicyRequest(ER_Incident incident);

    VehicleRequest createVehicleRequest(ER_Incident incident);

    CoveragesRequest createCoveragesRequest(ER_Incident incident);
    
    PrimeRequest createPrimeRequest(ER_Incident incident, PolicyResponse policy);
    
    PaymentMethodRequest createPaymentMethodRequest(ER_Incident incident);
    
    YoungerRequest createYoungerRequest(ER_Incident incident);
    
    WorkFlowRequest createWorkFlowRequest(ER_Incident incident);

}