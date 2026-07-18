package com.kressin.fitness_app.dto;

import java.util.List;

public record UpdateExercisePlansOrderCommand(
        Long workoutId,
        List<Long> exercisePlanIds) {
}
