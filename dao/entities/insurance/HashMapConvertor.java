package com.freecharge.financial.dao.entities.insurance;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freecharge.financial.dto.enums.OptionalParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HashMapConvertor implements AttributeConverter<Map<OptionalParams, Object>, String> {

    private static final Logger logger = LoggerFactory.getLogger(HashMapConvertor.class);

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<OptionalParams, Object> metaData) {

        String customerInfoJson = null;
        try {
            customerInfoJson = objectMapper.writeValueAsString(metaData);
        } catch (final JsonProcessingException e) {
            logger.error("JSON writing error", e);
        }

        return customerInfoJson;
    }

    @Override
    public Map<OptionalParams, Object> convertToEntityAttribute(String customerInfoJSON) {
        Map<OptionalParams, Object> metaData = null;
        try {
            if (customerInfoJSON == null) {
                metaData = new HashMap<>();
                return metaData;
            }
            metaData = objectMapper.readValue(customerInfoJSON, new TypeReference<Map<OptionalParams,Object>>(){});
        } catch (final IOException e) {
            logger.error("JSON reading error", e);
        }

        return metaData;
    }
}

