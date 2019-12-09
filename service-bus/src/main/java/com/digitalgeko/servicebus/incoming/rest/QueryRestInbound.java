package com.digitalgeko.servicebus.incoming.rest;

import com.digitalgeko.servicebus.service.QueryBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class QueryRestInbound {

    @Autowired
    private QueryBus queryBus;

    @PostMapping("/client")
    ResponseEntity<String> clientQuery(@RequestBody String message) {
        String response = queryBus.clientQuery(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/average")
    ResponseEntity<String> averageValueQuery(@RequestBody String message) {
        String response = queryBus.averageValueQuery(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/insurable")
    ResponseEntity<String> insurableVehicleQuery(@RequestBody String message) {
        String response = queryBus.insurableVehicleQuery(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/personDetail")
    ResponseEntity<String> personDetailQuery(@RequestBody String message) {
        String response = queryBus.personDetailQuery(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/businessDetail")
    ResponseEntity<String> businessDetailQuery(@RequestBody String message) {
        String response = queryBus.businessDetailQuery(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pendingTransactions")
    ResponseEntity<String> pendingTransactionsQuery(@RequestBody String message) {
        String response = queryBus.pendingTransactionsQuery(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/policyProduct")
    ResponseEntity<String> policyProductQuery(@RequestBody String message) {
        String response = queryBus.policyProduct(message);
        return ResponseEntity.ok(response);
    }

}
