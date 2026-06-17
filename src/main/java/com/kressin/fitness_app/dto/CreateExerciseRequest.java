package com.kressin.fitness_app.dto;

public record CreateExerciseRequest(
    String name,
    String description,
    String category,
    String muscleGroup
) {}
