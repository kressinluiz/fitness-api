package com.kressin.fitness_app.dto;

import java.util.List;

public record UpdateWorkoutRequest(
        Long id,
        String name,
        String description,
        List<UpdateExercisePlanRequest> exercisePlans) {
}
