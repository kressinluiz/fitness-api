package com.kressin.fitness_app.dto;

import java.util.List;

public record WorkoutResponse(
    Long id,
    String name,
    String description,
    List<ExercisePlanResponse> exercisePlans
) {}
