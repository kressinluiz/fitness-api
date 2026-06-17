package com.kressin.fitness_app.service.command;

public record CreateExerciseCommand(
    String name,
    String description,
    String category,
    String muscleGroup
) {}
