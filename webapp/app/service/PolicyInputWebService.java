package service;

import models.ws.*;

public interface PolicyInputWebService {

    PersonClientResponse sendPersonClient(PersonClientRequest criteria);
    BusinessClientResponse sendBusinessClient(BusinessClientRequest criteria);
    PayerResponse sendDataPayer(PayerRequest criteria);
    PolicyResponse sendDataPolicy(PolicyRequest criteria);
    VehicleResponse sendDataVehicle(VehicleRequest criteria);
    CoveragesResponse sendListCoverages(CoveragesRequest criteria);
    PrimeResponse sendPrimeList(PrimeRequest criteria);
    YoungerResponse sendCoveragesYounger(YoungerRequest criteria);
    PaymentMethodResponse sendPaymentMethod(PaymentMethodRequest criteria);
    WorkFlowResponse sendDataWorkFlow(WorkFlowRequest criteria);

}
