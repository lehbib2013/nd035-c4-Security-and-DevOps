package com.example.demo.handling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ApiError {
    private Date timestamp;
    private String message;
    private List<String> errors = new ArrayList<>();
    private String details;

    ApiError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
        this.details = "";
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public ApiError(Date timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}