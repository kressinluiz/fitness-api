package com.kressin.fitness_app.entity;

import java.time.ZonedDateTime;

import com.kressin.fitness_app.exception.BusinessException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ScheduleEntry {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workoutdate_id", nullable = false)
    private WorkoutDate workoutDate;

    @Column(nullable = false)
    private Integer weekDay;
    @Column(nullable = false)
    private ZonedDateTime dateTime;

    protected ScheduleEntry() {
    }

    public ScheduleEntry(Integer weekDay, ZonedDateTime dateTime, WorkoutDate workoutDate) {
        if (weekDay == null || weekDay < 0 || weekDay > 7) {
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

    public Integer getWeekDay() {
        return weekDay;
    }

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public WorkoutDate getWorkoutDate() {
        return workoutDate;
    }

    public void setWeekDay(Integer weekDay) {
        if (weekDay < 0 || weekDay > 7 || weekDay == null) {
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
