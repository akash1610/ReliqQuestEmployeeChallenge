package com.reliaquest.api.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiException extends  RuntimeException{
    private String message;
    public ApiException(String message){
        super(message);
    }
}
