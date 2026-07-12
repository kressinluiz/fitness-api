package com.kressin.fitness_app.entity;

import com.kressin.fitness_app.exception.BusinessException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class WorkoutPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private WorkoutDate workoutDate;

    protected WorkoutPlan() {
    }

    public WorkoutPlan(Workout workout) {
        if (workout == null) {
            throw new BusinessException("WorkoutPlan can't be created with null Workout");
        }
        this.workout = workout;
        workout.addWorkoutPlan(this);
    }

    public Long getId() {
        return id;
    }

    public Workout getWorkout() {
        return workout;
    }

    public WorkoutDate getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkout(Workout workout) {
        if (workout == null) {
            throw new BusinessException("Cannot set a null Workout for WorkoutPlan");
        }
        this.workout = workout;
        workout.addWorkoutPlan(this);
    }

    public void setWorkoutDate(WorkoutDate workoutDate) {
        if (workoutDate == null) {
            throw new BusinessException("Cannot set a null workoutDate for WorkoutPlan");
        }
        this.workoutDate = workoutDate;
    }
}
