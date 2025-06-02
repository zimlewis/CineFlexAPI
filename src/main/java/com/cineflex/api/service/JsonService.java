package com.cineflex.api.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonService {
    public <T> T getOrNull(JsonNode json, String field, Class<T> type) {
        try {
            JsonNode fieldNode = json.get(field);
            if (fieldNode == null || fieldNode.isNull()) {
                return null;
            }

            ObjectMapper o = new ObjectMapper();
            return o.treeToValue(fieldNode, type);
        }
        catch (JsonProcessingException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
