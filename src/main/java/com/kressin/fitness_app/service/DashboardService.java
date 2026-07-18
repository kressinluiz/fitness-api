package com.kressin.fitness_app.service;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.DashboardSummaryResponse;
import com.kressin.fitness_app.dto.UpcomingWorkoutResponse;
import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.mapper.DashboardMapper;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.repository.ScheduleEntryRepository;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;

import jakarta.transaction.Transactional;

@Service
public class DashboardService {
    private static final int UPCOMING_WORKOUTS_LIMIT = 5;

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final ScheduleEntryRepository scheduleEntryRepository;

    public DashboardService(
            ExerciseRepository exerciseRepository,
            WorkoutRepository workoutRepository,
            WorkoutPlanRepository workoutPlanRepository,
            ScheduleEntryRepository scheduleEntryRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workoutPlanRepository = workoutPlanRepository;
        this.scheduleEntryRepository = scheduleEntryRepository;
    }

    public long getExerciseCount() {
        return exerciseRepository.count();
    }

    public long getWorkoutCount() {
        return workoutRepository.count();
    }

    public long getWorkoutPlanCount() {
        return workoutPlanRepository.count();
    }

    @Transactional
    public List<UpcomingWorkoutResponse> getUpcomingWorkouts() {
        ZonedDateTime now = ZonedDateTime.now();

        Stream<UpcomingWorkoutResponse> specificDates = scheduleEntryRepository
                .findUpcomingSpecificDates(now).stream()
                .map(entry -> DashboardMapper.toUpcomingWorkoutResponse(entry, entry.getDateTime()));

        Stream<UpcomingWorkoutResponse> recurring = scheduleEntryRepository
                .findAllRecurring().stream()
                .map(entry -> DashboardMapper.toUpcomingWorkoutResponse(entry, nextOccurrence(entry, now)));

        return Stream.concat(specificDates, recurring)
                .sorted(Comparator.comparing(UpcomingWorkoutResponse::dateTime))
                .limit(UPCOMING_WORKOUTS_LIMIT)
                .toList();
    }

    private ZonedDateTime nextOccurrence(ScheduleEntry entry, ZonedDateTime now) {
        ZonedDateTime candidate = now
                .with(entry.getDateTime().toLocalTime())
                .with(TemporalAdjusters.nextOrSame(entry.getWeekDay()));

        return candidate.isBefore(now) ? candidate.plusWeeks(1) : candidate;
    }

    @Transactional
    public DashboardSummaryResponse getSummary() {
        return DashboardMapper.toSummaryResponse(
                getExerciseCount(),
                getWorkoutCount(),
                getWorkoutPlanCount(),
                getUpcomingWorkouts());
    }
}
