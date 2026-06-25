package com.kressin.fitness_app.service.command;

import java.util.List;

import com.kressin.fitness_app.entity.ScheduleType;

public record CreateWorkoutDateCommand(
        ScheduleType scheduleType,
        List<CreateScheduleEntryCommand> scheduleEntries) {
}
