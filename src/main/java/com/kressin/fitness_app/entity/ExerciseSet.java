package com.kressin.fitness_app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ExerciseSet {
    @Id
    @GeneratedValue
    private Long id;
    private Integer reps;
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "exerciseplan_id")
    private ExercisePlan exercisePlan;

    protected ExerciseSet() {
    }

    public ExerciseSet(Integer reps, Double weight, ExercisePlan plan) {
        if (reps == null || reps <= 0) {
            throw new IllegalArgumentException("ExerciseSet reps cannot be negative or zero");
        }
        if (weight == null || weight < 0) {
            throw new IllegalArgumentException("ExerciseSet weight cannot be negative");
        }
        if (plan == null) {
            throw new IllegalArgumentException("ExerciseSet plan cannot be null");
        }
        this.reps = reps;
        this.weight = weight;
        this.exercisePlan = plan;
    }

    public Long getId() {
        return id;
    }

    public Integer getReps() {
        return reps;
    }

    public Double getWeight() {
        return weight;
    }

    public ExercisePlan getExercisePlan() {
        return exercisePlan;
    }

    public void setReps(Integer reps) {
        if (reps <= 0) {
            throw new IllegalArgumentException("ExerciseSet reps cannot be negative or zero");
        }
        this.reps = reps;
    }

    public void setWeight(Double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("ExerciseSet weight cannot be negative or zero");
        }
        this.weight = weight;
    }
}
