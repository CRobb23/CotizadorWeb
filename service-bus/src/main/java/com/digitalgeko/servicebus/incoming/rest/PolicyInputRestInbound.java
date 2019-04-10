package com.digitalgeko.servicebus.incoming.rest;

import com.digitalgeko.servicebus.service.PolicyInputBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/input")
public class PolicyInputRestInbound {

    @Autowired
    private PolicyInputBus inputBus;

    @PostMapping("/person")
    ResponseEntity<String> personInput(@RequestBody String message) {
        String response = inputBus.personClientInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/business")
    ResponseEntity<String> businessInput(@RequestBody String message) {
        String response = inputBus.businessClientInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payer")
    ResponseEntity<String> payerInput(@RequestBody String message) {
        String response = inputBus.payerDataInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/policy")
    ResponseEntity<String> policyInput(@RequestBody String message) {
        String response = inputBus.policyDataInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/vehicle")
    ResponseEntity<String> vehicleInput(@RequestBody String message) {
        String response = inputBus.vehicleDataInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/coverage")
    ResponseEntity<String> coverageInput(@RequestBody String message) {
        String response = inputBus.coverageListInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/prime")
    ResponseEntity<String> primeInput(@RequestBody String message) {
        String response = inputBus.primeListInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/young")
    ResponseEntity<String> youngerCoverageInput(@RequestBody String message) {
        String response = inputBus.youngerCoveragesInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/payment")
    ResponseEntity<String> paymentInput(@RequestBody String message) {
        String response = inputBus.paymentMethodInput(message);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/workflow")
    ResponseEntity<String> workflowInput(@RequestBody String message) {
        String response = inputBus.workflowDataInput(message);
        return ResponseEntity.ok(response);
    }
}
