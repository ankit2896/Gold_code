package com.freecharge.financial.config;

import com.freecharge.financial.dto.enums.RequestHeaders;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SwaggerConfiguration {

    private Map<String, String> requestHeaderMetaData = new HashMap<String, String>() {
        {
            put(RequestHeaders.COOKIE.toString(), "app_fc=B8Ot0xIdkamHpickVj9dUGhGmBQLLjGUVefJg9yqSfM0J8mmf9DRH7t1IzMDqaLnyr6GNQimMauPCDdYdqFV374IzN9cptvxlo5r0JnItikCOGb1F-LyXsui2dkWmiar; fc.tc3=1c08eb38-7238-4783-bb69-2c0c39cb706e; fc.tv=920f803f-7bf9-40a8-ae55-a88cdb8829b3");
            put(RequestHeaders.CSRF_ID_REQUEST_IDENTIFIER.toString(), "0ce5ac85-1750-453b-a5f6-f764553727cf");
            put(RequestHeaders.X_NEWRelic_ID.toString(), "VwQGVlVWABABVFJSBwICUFAD");
        }
    };

    private boolean isOtionalHeader = false;

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> operation
                .addParametersItem(new Parameter().in(RequestHeaders.HEADER.toString()).required(isOtionalHeader).name(RequestHeaders.COOKIE.toString()).example(requestHeaderMetaData.get(RequestHeaders.COOKIE.toString())))
                .addParametersItem(new Parameter().in(RequestHeaders.HEADER.toString()).required(isOtionalHeader).name(RequestHeaders.CSRF_ID_REQUEST_IDENTIFIER.toString()).example(requestHeaderMetaData.get(RequestHeaders.CSRF_ID_REQUEST_IDENTIFIER.toString())))
                .addParametersItem(new Parameter().in(RequestHeaders.HEADER.toString()).required(isOtionalHeader).name(RequestHeaders.X_NEWRelic_ID.toString()).example(requestHeaderMetaData.get(RequestHeaders.X_NEWRelic_ID.toString())));
    }


}
