package com.kressin.fitness_app.service.command;

import java.util.List;

public record CreateWorkoutCommand(
    String name,
    String description,
    List<CreateExercisePlanCommand> exercisePlans
) {}
