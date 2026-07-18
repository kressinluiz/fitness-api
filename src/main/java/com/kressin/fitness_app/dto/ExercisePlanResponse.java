package com.kressin.fitness_app.dto;

import java.util.List;

public record ExercisePlanResponse(
        Long id,
        Integer position,
        ExerciseResponse exercise,
        List<ExerciseSetResponse> exerciseSets) {
}
