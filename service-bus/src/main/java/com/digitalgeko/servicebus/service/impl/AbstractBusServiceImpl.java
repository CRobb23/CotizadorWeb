package com.digitalgeko.servicebus.service.impl;

import com.digitalgeko.servicebus.exceptions.CSVMarshalException;
import com.digitalgeko.servicebus.exceptions.ConvertException;
import com.digitalgeko.servicebus.exceptions.JSONMarshalException;
import com.digitalgeko.servicebus.exceptions.XMLMarshalException;
import com.digitalgeko.servicebus.util.CSVMarshalUtil;
import com.digitalgeko.servicebus.util.JSONMarshalUtil;
import com.digitalgeko.servicebus.util.XMLMarshalUtil;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;

public abstract class AbstractBusServiceImpl {

    @Autowired
    protected Mapper mapper;

    protected static final Logger log = LoggerFactory.getLogger(AutoInspectionsBusServiceImpl.class);

    protected String fromSOAPtoJSON(String soapMessage, Class soapClass, Class restClass) throws ConvertException {
        try {
            log.info("SOAP >: " + soapMessage);
            Object soapObj =  XMLMarshalUtil.fromXML(soapClass, soapMessage);
            Object restObj = mapper.map(soapObj, restClass);
            // Check if Message is Successful and set variable according to it
            restObj = checkSuccess(restObj, soapObj);
            String restMessage = JSONMarshalUtil.toJSON(restObj);
            log.info("REST <: " + restMessage);
            return restMessage;
        } catch (XMLMarshalException | JSONMarshalException e) {
            log.error(e.getMessage(), e);
            throw new ConvertException("PROBLEMA DE CONVERSION SOAP/JSON", e);
        }
    }

    protected String fromJSONtoSOAP(String restMessage, Class restClass, Class soapClass) throws ConvertException {
        try {
            log.info("REST >: " + restMessage);
            Object restObj = JSONMarshalUtil.fromJSON(restClass, restMessage);
            Object soapObj = mapper.map(restObj, soapClass);
            String soapMessage =  XMLMarshalUtil.toXML(soapClass, soapObj);
            log.info("SOAP <: " + soapMessage);
            return soapMessage;
        } catch (XMLMarshalException | JSONMarshalException e) {
            log.error(e.getMessage(), e);
            throw new ConvertException("PROBLEMA DE CONVERSION JSON/SOAP", e);
        }
    }

    protected String fromCSVtoJSON(String csvMessage, Class csvClass, Class restClass) throws ConvertException {
        try {
            log.info("CSV >: " + csvMessage);
            Object csvObj =  CSVMarshalUtil.fromCSV(csvClass, csvMessage);
            Object restObj = mapper.map(csvObj, restClass);
            String restMessage = JSONMarshalUtil.toJSON(restObj);
            log.info("REST <: " + restMessage);
            return restMessage;
        } catch (CSVMarshalException | JSONMarshalException e) {
            log.error(e.getMessage(), e);
            throw new ConvertException("PROBLEMA DE CONVERSION CSV/JSON", e);
        }
    }

    protected String fromJSONtoCSV(String restMessage, Class restClass, Class csvClass) throws ConvertException {
        try {
            log.info("REST >: " + restMessage);
            Object restObj = JSONMarshalUtil.fromJSON(restClass, restMessage);
            Object csvObj = mapper.map(restObj, csvClass);
            String csvMessage =  CSVMarshalUtil.toCSV(csvClass, csvObj);
            log.info("CSV <: " + csvMessage);
            return csvMessage;
        } catch (CSVMarshalException | JSONMarshalException e) {
            log.error(e.getMessage(), e);
            throw new ConvertException("PROBLEMA DE CONVERSION JSON/CSV", e);
        }
    }

    private Object checkSuccess(Object restObj, Object soapObj) {
        try {
            // Check if soapObj has Message Field
            Field messageField = soapObj.getClass().getField("message");
            // Check if restObj has Success Field
            Field successField = restObj.getClass().getField("success");
            // Check if Message from soapObj is Successful
            String messageValue = (String) messageField.get(soapObj);
            if ("SATISFACTORIO".equals(messageValue)) {
                successField.setBoolean(restObj, Boolean.TRUE);
            }
        } catch (Exception e) {
            log.debug("Check Success Not Available for this conversion... IGNORING CHECK");
        }
        return restObj;
    }
}
