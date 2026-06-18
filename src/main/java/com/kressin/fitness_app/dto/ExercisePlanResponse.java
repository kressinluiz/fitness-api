package com.kressin.fitness_app.dto;

import java.util.List;

public record ExercisePlanResponse(
    Long id,
    ExerciseResponse exercise,
    List<ExerciseSetResponse> exerciseSets
) {}
