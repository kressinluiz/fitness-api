package com.kressin.fitness_app.dto;

import java.util.List;

public record DashboardSummaryResponse(
        long exerciseCount,
        long workoutCount,
        long workoutPlanCount,
        List<UpcomingWorkoutResponse> upcomingWorkouts) {
}
