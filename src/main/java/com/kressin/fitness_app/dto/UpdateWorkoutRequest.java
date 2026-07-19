package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record UpdateWorkoutRequest(
        @Size(min = 1, max = 100) String name,
        @Size(min = 1, max = 500) String description,
        @Valid List<UpdateExercisePlanRequest> exercisePlans) {
}
