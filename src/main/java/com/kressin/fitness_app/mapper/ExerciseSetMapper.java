package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.CreateExerciseSetRequest;
import com.kressin.fitness_app.dto.ExerciseSetResponse;
import com.kressin.fitness_app.dto.UpdateExerciseSetRequest;
import com.kressin.fitness_app.entity.ExerciseSet;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;

public class ExerciseSetMapper {
    public static ExerciseSetResponse toResponse(ExerciseSet set) {
        return new ExerciseSetResponse(
                set.getId(),
                set.getReps(),
                set.getWeight());
    }

    public static List<ExerciseSetResponse> toResponseList(List<ExerciseSet> sets) {
        return sets.stream().map(ExerciseSetMapper::toResponse).toList();
    }

    public static CreateExerciseSetCommand toCreateCommand(CreateExerciseSetRequest set) {
        return new CreateExerciseSetCommand(
                set.reps(),
                set.weight());
    }

    public static List<CreateExerciseSetCommand> toCreateCommandList(List<CreateExerciseSetRequest> sets) {
        return sets.stream().map(ExerciseSetMapper::toCreateCommand).toList();
    }

    public static UpdateExerciseSetCommand toUpdateCommand(UpdateExerciseSetRequest set) {
        return set != null ? new UpdateExerciseSetCommand(
                set.id(),
                set.shouldDelete() != null ? set.shouldDelete() : null,
                set.reps() != null ? set.reps() : null,
                set.weight() != null ? set.weight() : null) : null;
    }

    public static List<UpdateExerciseSetCommand> toUpdateCommandList(List<UpdateExerciseSetRequest> sets) {
        return sets.stream().map(ExerciseSetMapper::toUpdateCommand).toList();
    }

    public static CreateExerciseSetCommand toCreateCommandFromUpdateCommand(UpdateExerciseSetCommand set) {
        return new CreateExerciseSetCommand(
                set.reps(),
                set.weight());
    }

    public static List<CreateExerciseSetCommand> toCreateCommandListFromUpdateCommand(
            List<UpdateExerciseSetCommand> sets) {
        return sets != null ? sets.stream().map(ExerciseSetMapper::toCreateCommandFromUpdateCommand).toList() : null;
    }
}
