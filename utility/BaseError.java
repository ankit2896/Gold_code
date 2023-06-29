package com.freecharge.financial.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseError {
    private String errorCode;
    private String errorMessage;
}
