package com.kressin.fitness_app.dto;

// maybe I dont need to return id here
public record ExerciseSetResponse(
        Long id,
        Integer reps,
        Double weight) {
}
