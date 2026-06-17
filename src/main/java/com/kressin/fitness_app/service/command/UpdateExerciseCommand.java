package com.kressin.fitness_app.service.command;

public record UpdateExerciseCommand(
    Long   id,
    String name,
    String description,
    String category,
    String muscleGroup
) {}
