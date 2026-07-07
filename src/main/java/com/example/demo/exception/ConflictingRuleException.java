package com.example.demo.exception;

public class ConflictingRuleException extends RuntimeException {
    public ConflictingRuleException(String message) {
        super(message);
    }
}