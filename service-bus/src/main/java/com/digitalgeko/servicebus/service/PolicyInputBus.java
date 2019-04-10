package com.digitalgeko.servicebus.service;

public interface PolicyInputBus {

    String personClientInput(String message);

    String businessClientInput(String message);

    String payerDataInput(String message);

    String policyDataInput(String message);

    String vehicleDataInput(String message);

    String coverageListInput(String message);

    String primeListInput(String message);

    String youngerCoveragesInput(String message);

    String paymentMethodInput(String message);

    String workflowDataInput(String message);
}
