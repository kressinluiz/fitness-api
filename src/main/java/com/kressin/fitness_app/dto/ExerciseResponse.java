package com.kressin.fitness_app.dto;

public record ExerciseResponse(
    Long id,
    String name,
    String description,
    String category,
    String muscleGroup
) {
    
}
