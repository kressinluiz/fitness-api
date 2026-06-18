package com.kressin.fitness_app.service.command;

import java.util.List;

public record CreateExercisePlanCommand(
    Long exerciseId,
    List<CreateExerciseSetCommand> sets
) {}
