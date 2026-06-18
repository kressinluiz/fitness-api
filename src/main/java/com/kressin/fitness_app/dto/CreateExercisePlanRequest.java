package com.kressin.fitness_app.dto;

import java.util.List;

public record CreateExercisePlanRequest(
    Long exerciseId,
    List<CreateExerciseSetRequest> sets
) {}
