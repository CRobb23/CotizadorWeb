package com.digitalgeko.servicebus.service;

public interface AutoInspectionsBus {

    String updateInspectionState(String message);

    String createAutoInspection(String message);
}
