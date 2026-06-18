package com.kressin.fitness_app.service.command;

import java.util.List;

public record UpdateWorkoutCommand(
    Long id,
    String name,
    String description,
    List<UpdateExercisePlanCommand> exercisePlans) {
}
