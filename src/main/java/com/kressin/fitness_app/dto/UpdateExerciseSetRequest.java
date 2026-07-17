package com.kressin.fitness_app.dto;

public record UpdateExerciseSetRequest(
        Long id,
        Boolean shouldDelete,
        Integer reps,
        Double weight) {
}
