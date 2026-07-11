package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.CreateWorkoutRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutRequest;
import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.service.command.CreateWorkoutCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutCommand;

public class WorkoutMapper {
    public static WorkoutResponse toResponse(Workout workout) {
        return new WorkoutResponse(
                workout.getId(),
                workout.getName(),
                workout.getDescription(),
                ExercisePlanMapper.toResponseList(workout.getExercisePlans()));
    }

    public static List<WorkoutResponse> toResponseList(List<Workout> workouts) {
        return workouts.stream().map(WorkoutMapper::toResponse).toList();
    }

    public static CreateWorkoutCommand toCreateCommand(CreateWorkoutRequest workout) {
        return new CreateWorkoutCommand(
                workout.name(),
                workout.description(),
                workout.exercisePlans() != null ? ExercisePlanMapper.toCreateCommandList(workout.exercisePlans())
                        : null);
    }

    public static UpdateWorkoutCommand toUpdateCommand(UpdateWorkoutRequest workout, Long id) {
        return new UpdateWorkoutCommand(
                id,
                workout.name() != null ? workout.name() : null,
                workout.description() != null ? workout.description() : null,
                workout.exercisePlans() != null ? ExercisePlanMapper.toUpdateCommandList(workout.exercisePlans())
                        : null);
    }
}
