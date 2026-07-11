package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.CreateExerciseRequest;
import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.dto.UpdateExerciseRequest;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.service.command.CreateExerciseCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseCommand;

public class ExerciseMapper {
    public static ExerciseResponse toResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getCategory(),
                exercise.getMuscleGroup());
    }

    public static List<ExerciseResponse> toResponseList(List<Exercise> exercises) {
        return exercises.stream().map(ExerciseMapper::toResponse).toList();
    }

    public static CreateExerciseCommand toCreateCommand(CreateExerciseRequest request) {
        return new CreateExerciseCommand(
                request.name(),
                request.description(),
                request.category(),
                request.muscleGroup());
    }

    public static UpdateExerciseCommand toUpdateCommand(UpdateExerciseRequest request, Long id) {
        return new UpdateExerciseCommand(
                id,
                request.name() != null ? request.name() : null,
                request.description() != null ? request.description() : null,
                request.category() != null ? request.category() : null,
                request.muscleGroup() != null ? request.muscleGroup() : null);
    }
}
