package com.kressin.fitness_app.service.command;

public record CreateExerciseSetCommand(
    Integer reps,
    Double weight
) {}
