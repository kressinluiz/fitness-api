package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;

public record UpdateWorkoutRequest(
        String name,
        String description,
        @Valid List<UpdateExercisePlanRequest> exercisePlans) {
}
