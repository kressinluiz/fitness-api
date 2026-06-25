package com.kressin.fitness_app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class WorkoutPlan {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @OneToOne(mappedBy = "workoutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private WorkoutDate workoutDate;

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
        this.workout = workout;
    }

    public void setWorkoutDate(WorkoutDate workoutDate) {
        this.workoutDate = workoutDate;
    }
}
