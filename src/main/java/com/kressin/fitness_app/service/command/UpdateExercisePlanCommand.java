package com.kressin.fitness_app.service.command;

import java.util.List;

public record UpdateExercisePlanCommand(
    Long id,
    Long exerciseId,
    List<UpdateExerciseSetCommand> sets) {
}
