package com.reliaquest.api.exception;

import com.reliaquest.api.Exception.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ErrorResponseTest {

    @Test
    void testAllArgsConstructor() {
        String message = "Error message";
        String param = "Param1";
        ErrorResponse<String> errorResponse = new ErrorResponse<>(message, param);

        assertEquals(message, errorResponse.getMessage());
        assertEquals(param, errorResponse.getErrorIdentificationParam());
    }

    @Test
    void testNoArgsConstructor() {
        ErrorResponse<String> errorResponse = new ErrorResponse<>();

        assertNull(errorResponse.getMessage());
        assertNull(errorResponse.getErrorIdentificationParam());
    }

    @Test
    void testSingleArgConstructor() {
        String message = "Single error message";
        ErrorResponse<String> errorResponse = new ErrorResponse<>(message);

        assertEquals(message, errorResponse.getMessage());
        assertNull(errorResponse.getErrorIdentificationParam());
    }

    @Test
    void testSettersAndGetters() {
        String message = "Set error message";
        String param = "Param2";
        ErrorResponse<String> errorResponse = new ErrorResponse<>();

        errorResponse.setMessage(message);
        errorResponse.setErrorIdentificationParam(param);

        assertEquals(message, errorResponse.getMessage());
        assertEquals(param, errorResponse.getErrorIdentificationParam());
    }
}
