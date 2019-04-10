package com.digitalgeko.servicebus.util;

import com.digitalgeko.servicebus.exceptions.JSONMarshalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONMarshalUtil {

    private static final Logger log = LoggerFactory.getLogger(JSONMarshalUtil.class);

    public static String toJSON(Object object) throws JSONMarshalException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JSONMarshalException(e.getMessage(), e);
        }
    }

    public static Object fromJSON(Class clazz, String message) throws JSONMarshalException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(message, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JSONMarshalException(e.getMessage(), e);
        }
    }
}
