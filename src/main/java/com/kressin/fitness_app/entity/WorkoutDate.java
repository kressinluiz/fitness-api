package com.kressin.fitness_app.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kressin.fitness_app.exception.BusinessException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class WorkoutDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "workoutDate")
    @JoinColumn(name = "workout_plan_id")
    private WorkoutPlan workoutPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType scheduleType;

    @OneToMany(mappedBy = "workoutDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleEntry> scheduleEntries = new ArrayList<>();

    protected WorkoutDate() {
    }

    public WorkoutDate(WorkoutPlan workoutPlan, ScheduleType scheduleType) {
        if (workoutPlan == null) {
            throw new BusinessException("Cannot create WorkoutDate with null WorkoutPlan");
        }
        if (scheduleType == null) {
            throw new BusinessException("Cannot create WorkoutDate with null ScheduleType");
        }
        this.scheduleType = scheduleType;
        this.workoutPlan = workoutPlan;
    }

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
        return Collections.unmodifiableList(scheduleEntries);
    }

    public void addScheduleEntry(ScheduleEntry entry) {
        if (entry == null) {
            throw new BusinessException("Cannot add null ScheduleEntry to WorkoutDate");
        }
        scheduleEntries.add(entry);
    }

    public void removeScheduleEntry(ScheduleEntry entry) {
        if (entry == null) {
            throw new BusinessException("Cannot remove null ScheduleEntry from WorkoutDate");
        }
        if (!scheduleEntries.remove(entry)) {
            throw new BusinessException("Why are we trying to remove a ScheduleEntry not present in the list?");
        }
    }

    public void setScheduleType(ScheduleType scheduleType) {
        if (scheduleType == null) {
            throw new BusinessException("Cannot create WorkoutDate with null ScheduleType");
        }
        this.scheduleType = scheduleType;
    }
}
