package com.kressin.fitness_app.repository;

import java.time.ZonedDateTime;
import java.util.List;

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
            WHERE wd.scheduleType = com.kressin.fitness_app.entity.ScheduleType.SPECIFIC_DATES
              AND se.dateTime > :now
            """)
    List<ScheduleEntry> findUpcomingSpecificDates(@Param("now") ZonedDateTime now);

    @Query("""
            SELECT se FROM ScheduleEntry se
            JOIN FETCH se.workoutDate wd
            JOIN FETCH wd.workoutPlan wp
            JOIN FETCH wp.workout w
            WHERE wd.scheduleType = com.kressin.fitness_app.entity.ScheduleType.RECURRING
            """)
    List<ScheduleEntry> findAllRecurring();
}
