package com.kressin.fitness_app.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kressin.fitness_app.entity.ScheduleEntry;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {
    @Query("""
            SELECT se FROM ScheduleEntry se
            JOIN FETCH se.workoutDate wd
            JOIN FETCH wd.workoutPlan wp
            JOIN FETCH wp.workout w
            WHERE se.dateTime > :now
            ORDER BY se.dateTime ASC
            """)
    List<ScheduleEntry> findUpcomingWithWorkoutPlan(
            @Param("now") ZonedDateTime now,
            Pageable pageable);
}

