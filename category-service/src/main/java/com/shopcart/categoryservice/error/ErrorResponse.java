package com.shopcart.categoryservice.error;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private String errorMessage;
    private HttpStatus status;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    // Fixed getMessage() method
    public String getMessage() {
        return errorMessage; 
    }
}
