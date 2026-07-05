package com.kressin.fitness_app.dto;

import java.util.List;

public record UpdateExercisePlanRequest(
        Long id,
        Boolean shouldDelete,
        Long exerciseId,
        List<UpdateExerciseSetRequest> sets) {
}
