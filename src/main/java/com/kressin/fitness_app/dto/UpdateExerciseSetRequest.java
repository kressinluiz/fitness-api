package com.kressin.fitness_app.dto;

public record UpdateExerciseSetRequest(
    Long id,
    Integer reps,
    Double weight) {
}
