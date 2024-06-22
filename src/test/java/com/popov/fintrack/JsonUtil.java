package com.popov.fintrack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule()); // To support Java 8 date and time API
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON to " + clazz.getSimpleName(), e);
        }
    }

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON to List<" + clazz.getSimpleName() + ">", e);
        }
    }

    public static <T> String writeValue(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to write " + obj.getClass().getSimpleName() + " to JSON", e);
        }
    }

    public static <T> String writeAdditionProps(T obj, String additionalPropName, Object additionalPropValue) {
        try {
            String json = OBJECT_MAPPER.writeValueAsString(obj);
            ObjectNode node = OBJECT_MAPPER.readValue(json, ObjectNode.class);
            node.putPOJO(additionalPropName, additionalPropValue);
            return OBJECT_MAPPER.writeValueAsString(node);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write additional properties to " + obj.getClass().getSimpleName(), e);
        }
    }
}
