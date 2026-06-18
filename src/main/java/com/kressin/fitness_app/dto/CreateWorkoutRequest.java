package com.kressin.fitness_app.dto;

import java.util.List;

public record CreateWorkoutRequest(
    String name,
    String description,
    List<CreateExercisePlanRequest> exercisePlans
) {}
