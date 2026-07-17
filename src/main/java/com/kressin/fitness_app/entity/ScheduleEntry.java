package com.kressin.fitness_app.entity;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

import com.kressin.fitness_app.exception.BusinessException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ScheduleEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_date_id", nullable = false)
    private WorkoutDate workoutDate;

    @Column(nullable = false)
    private DayOfWeek weekDay;

    @Column(nullable = false)
    private ZonedDateTime dateTime;

    protected ScheduleEntry() {
    }

    public ScheduleEntry(DayOfWeek weekDay, ZonedDateTime dateTime, WorkoutDate workoutDate) {
        if (weekDay == null) {
            throw new BusinessException(String.format("Invalid WeekDay for ScheduleEntry: %d", weekDay));
        }
        if (dateTime == null) {
            throw new BusinessException("Null DateTime for ScheduleEntry");
        }
        if (workoutDate == null) {
            throw new BusinessException("Null WorkoutDate for ScheduleEntry");
        }

        this.weekDay = weekDay;
        this.dateTime = dateTime;
        this.workoutDate = workoutDate;
    }

    public Long getId() {
        return id;
    }

    public DayOfWeek getWeekDay() {
        return weekDay;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public WorkoutDate getWorkoutDate() {
        return workoutDate;
    }

    public void setWeekDay(DayOfWeek weekDay) {
        if (weekDay == null) {
            throw new BusinessException(String.format("Invalid WeekDay for ScheduleEntry: %d", weekDay));
        }
        this.weekDay = weekDay;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        if (dateTime == null) {
            throw new BusinessException("Null DateTime for ScheduleEntry");
        }
        this.dateTime = dateTime;
    }
}
