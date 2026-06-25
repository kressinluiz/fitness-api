package com.kressin.fitness_app.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class WorkoutDate {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "workoutplan_id")
    private WorkoutPlan workoutPlan;

    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @OneToMany(mappedBy = "workoutDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntry> scheduleEntries = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public WorkoutPlan getWorkoutPlan() {
        return workoutPlan;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public List<ScheduleEntry> getScheduleEntries() {
        return scheduleEntries;
    }

    public void setWorkoutPlan(WorkoutPlan workoutPlan) {
        this.workoutPlan = workoutPlan;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public void setScheduleEntries(List<ScheduleEntry> scheduleEntries) {
        this.scheduleEntries = scheduleEntries;
    }
}
