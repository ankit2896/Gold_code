package com.freecharge.financial.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseSRO<T> {
    private BaseError error;
    private T data;
}
