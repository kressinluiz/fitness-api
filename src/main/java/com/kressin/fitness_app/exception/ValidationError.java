package com.kressin.fitness_app.exception;

public record ValidationError(
        String field,
        String message) {
}
