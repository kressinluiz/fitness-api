package com.kressin.fitness_app.dto;

import java.util.List;

public record UpdateExercisePlansOrderRequest(
        List<Long> exercisePlanIds) {
}
