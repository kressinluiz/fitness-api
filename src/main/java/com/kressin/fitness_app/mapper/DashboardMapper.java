package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.DashboardSummaryResponse;
import com.kressin.fitness_app.dto.UpcomingWorkoutResponse;
import com.kressin.fitness_app.entity.ScheduleEntry;

public class DashboardMapper {
    public static UpcomingWorkoutResponse toUpcomingWorkoutResponse(ScheduleEntry entry) {
        return new UpcomingWorkoutResponse(
                entry.getWorkoutDate().getWorkoutPlan().getId(),
                entry.getWorkoutDate().getWorkoutPlan().getWorkout().getName(),
                entry.getWorkoutDate().getScheduleType(),
                entry.getWeekDay(),
                entry.getDateTime());
    }

    public static List<UpcomingWorkoutResponse> toUpcomingWorkoutResponseList(List<ScheduleEntry> entries) {
        return entries.stream().map(DashboardMapper::toUpcomingWorkoutResponse).toList();
    }

    public static DashboardSummaryResponse toSummaryResponse(
            long exerciseCount,
            long workoutCount,
            long workoutPlanCount,
            List<UpcomingWorkoutResponse> upcomingWorkouts) {
        return new DashboardSummaryResponse(
                exerciseCount,
                workoutCount,
                workoutPlanCount,
                upcomingWorkouts);
    }
}
