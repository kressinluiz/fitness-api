package com.kressin.fitness_app.dto;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

import com.kressin.fitness_app.entity.ScheduleType;

public record UpcomingWorkoutResponse(
        Long workoutPlanId,
        String workoutName,
        ScheduleType scheduleType,
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {
}
