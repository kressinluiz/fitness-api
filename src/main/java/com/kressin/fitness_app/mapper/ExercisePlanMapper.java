package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.CreateExercisePlanRequest;
import com.kressin.fitness_app.dto.ExercisePlanResponse;
import com.kressin.fitness_app.dto.UpdateExercisePlanRequest;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;

public class ExercisePlanMapper {
    public static ExercisePlanResponse toResponse(ExercisePlan plan) {
        return new ExercisePlanResponse(
                plan.getId(),
                ExerciseMapper.toResponse(plan.getExercise()),
                ExerciseSetMapper.toResponseList(plan.getSets()));
    }

    public static List<ExercisePlanResponse> toResponseList(List<ExercisePlan> plans) {
        return plans.stream().map(ExercisePlanMapper::toResponse).toList();
    }

    public static CreateExercisePlanCommand toCreateCommand(CreateExercisePlanRequest plan) {
        return new CreateExercisePlanCommand(
                plan.exerciseId(),
                ExerciseSetMapper.toCreateCommandList(plan.sets()));
    }

    public static List<CreateExercisePlanCommand> toCreateCommandList(List<CreateExercisePlanRequest> plans) {
        return plans.stream().map(ExercisePlanMapper::toCreateCommand).toList();
    }

    public static UpdateExercisePlanCommand toUpdateCommand(UpdateExercisePlanRequest plan) {
        return plan != null ? new UpdateExercisePlanCommand(
                plan.id(),
                plan.shouldDelete() != null ? plan.shouldDelete() : null,
                plan.exerciseId() != null ? plan.exerciseId() : null,
                ExerciseSetMapper.toUpdateCommandList(plan.sets())) : null;
    }

    public static List<UpdateExercisePlanCommand> toUpdateCommandList(List<UpdateExercisePlanRequest> plans) {
        return plans.stream().map(ExercisePlanMapper::toUpdateCommand).toList();
    }

    public static CreateExercisePlanCommand toCreateCommandFromUpdateCommand(UpdateExercisePlanCommand plan) {
        return new CreateExercisePlanCommand(
                plan.exerciseId(),
                ExerciseSetMapper.toCreateCommandListFromUpdateCommand(plan.sets()));
    }
}
