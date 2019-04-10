package com.digitalgeko.servicebus.incoming.rest;

import com.digitalgeko.servicebus.service.AutoInspectionsBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inspection")
public class InspectionRestInbound {

    @Autowired
    private AutoInspectionsBus inspectionsBus;

    @PostMapping("/auto")
    ResponseEntity<String> createAutoInspection(@RequestBody String message) {
        String response = inspectionsBus.createAutoInspection(message);
        return ResponseEntity.ok(response);
    }

}
