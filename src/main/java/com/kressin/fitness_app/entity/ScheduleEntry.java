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
        this.weekDay = weekDay;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setWorkoutDate(WorkoutDate workoutDate) {
        this.workoutDate = workoutDate;
    }
}
