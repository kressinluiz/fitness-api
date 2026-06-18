package com.kressin.fitness_app.dto;

public record CreateExerciseSetRequest(
        Integer reps,
        Double weight) {
}
