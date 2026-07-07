package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.CreateWorkoutPlanRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutPlanRequest;
import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.service.command.CreateWorkoutPlanCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutPlanCommand;

public class WorkoutPlanMapper {
    public static CreateWorkoutPlanCommand toCreateCommand(CreateWorkoutPlanRequest request) {
        return new CreateWorkoutPlanCommand(
                request.workoutId(),
                WorkoutDateMapper.toCreateCommand(request.workoutDate()));
    }

    public static UpdateWorkoutPlanCommand toUpdateCommand(UpdateWorkoutPlanRequest request, Long id) {
        return new UpdateWorkoutPlanCommand(
                id,
                request.workoutId(),
                WorkoutDateMapper.toUpdateCommand(request.workoutDate()));
    }

    public static WorkoutPlanResponse toResponse(WorkoutPlan plan) {
        return new WorkoutPlanResponse(
                plan.getId(),
                WorkoutMapper.toResponse(plan.getWorkout()),
                WorkoutDateMapper.toResponse(plan.getWorkoutDate()));
    }

    public static List<WorkoutPlanResponse> toResponseList(List<WorkoutPlan> plans) {
        return plans.stream().map(WorkoutPlanMapper::toResponse).toList();
    }
}
