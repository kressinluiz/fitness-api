package com.kressin.fitness_app.dto;

public record UpdateExerciseRequest(
        String name,
        String description,
        String category,
        String muscleGroup) {
}
