package com.kressin.fitness_app.entity;

import java.time.ZonedDateTime;

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

    @ManyToOne
    @JoinColumn(name = "workoutdate_id")
    private WorkoutDate workoutDate;

    private Integer weekDay;
    private ZonedDateTime dateTime;

    protected ScheduleEntry() {
    }

    public ScheduleEntry(Integer weekDay, ZonedDateTime dateTime, WorkoutDate workoutDate) {
        if (weekDay < 0 || weekDay > 7 || weekDay == null) {
            throw new IllegalArgumentException(String.format("Invalid WeekDay for ScheduleEntry: %d", weekDay));
        }
        if (dateTime == null) {
            throw new IllegalArgumentException("Null DateTime for ScheduleEntry");
        }
        if (workoutDate == null) {
            throw new IllegalArgumentException("Null WorkoutDate for ScheduleEntry");
        }

        this.weekDay = weekDay;
        this.dateTime = dateTime;
        this.workoutDate = workoutDate;
        workoutDate.addScheduleEntry(this);
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
            throw new IllegalArgumentException(String.format("Invalid WeekDay for ScheduleEntry: %d", weekDay));
        }
        this.weekDay = weekDay;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Null DateTime for ScheduleEntry");
        }
        this.dateTime = dateTime;
    }
}
