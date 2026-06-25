package com.kressin.fitness_app.mapper;

import com.kressin.fitness_app.dto.CreateWorkoutDateRequest;
import com.kressin.fitness_app.dto.UpdateWorkoutDateRequest;
import com.kressin.fitness_app.dto.WorkoutDateResponse;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.service.command.CreateWorkoutDateCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutDateCommand;

public class WorkoutDateMapper {
    public static CreateWorkoutDateCommand toCreateCommand(CreateWorkoutDateRequest request) {
        return new CreateWorkoutDateCommand(
                request.scheduleType(),
                ScheduleEntryMapper.toCreateCommandList(request.scheduleEntries()));
    }

    public static UpdateWorkoutDateCommand toUpdateCommand(UpdateWorkoutDateRequest request) {
        return new UpdateWorkoutDateCommand(
            request.id(),
            request.scheduleType(),
            ScheduleEntryMapper.toUpdateCommandList(request.scheduleEntries())
        );
    }

    public static WorkoutDateResponse toResponse(WorkoutDate workoutDate) {
        return new WorkoutDateResponse(
                workoutDate.getId(),
                workoutDate.getScheduleType(),
                ScheduleEntryMapper.toResponseList(workoutDate.getScheduleEntries()));
    }
}
