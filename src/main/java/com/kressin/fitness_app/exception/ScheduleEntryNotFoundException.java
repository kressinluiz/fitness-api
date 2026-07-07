package com.kressin.fitness_app.exception;

public class ScheduleEntryNotFoundException extends RuntimeException {
    public ScheduleEntryNotFoundException(Long id) {
        super("ScheduleEntry with id %d not found".formatted(id));
    }
}
