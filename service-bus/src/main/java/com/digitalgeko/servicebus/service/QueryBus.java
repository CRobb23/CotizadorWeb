package com.digitalgeko.servicebus.service;

public interface QueryBus {

    String clientQuery(String message);

    String averageValueQuery(String message);

    String insurableVehicleQuery(String message);

    String personDetailQuery(String message);

    String businessDetailQuery(String message);

    String pendingTransactionsQuery(String message);

    String policyProduct(String message);
}
