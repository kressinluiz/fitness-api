package com.kressin.fitness_app.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWorkoutRequest(
        @NotBlank @Size(min = 1, max = 100) String name,
        @NotNull @Size(min = 1, max = 500) String description,
        List<@Valid CreateExercisePlanRequest> exercisePlans) {
}
