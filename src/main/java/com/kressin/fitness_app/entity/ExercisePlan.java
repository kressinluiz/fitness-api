package com.kressin.fitness_app.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ExercisePlan {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @OneToMany(mappedBy = "exercisePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSet> sets = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "workout_id")
    private Workout workout;

    protected ExercisePlan() {
    }

    public ExercisePlan(Exercise exercise, Workout workout) {
        if (exercise == null) {
            throw new IllegalArgumentException("ExercisePlan must have a valid Exercise");
        }
        if (workout == null) {
            throw new IllegalArgumentException("ExercisePlan must have a valid Workout");
        }
        this.exercise = exercise;
        this.workout = workout;
    }

    public Long getId() {
        return id;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public Workout getWorkout() {
        return workout;
    }

    public List<ExerciseSet> getSets() {
        return Collections.unmodifiableList(sets);
    }

    public void addExerciseSet(ExerciseSet set) {
        if (set == null) {
            throw new IllegalArgumentException("ExerciseSet cannot be null");
        }
        this.sets.add(set);
    }

    public void removeExerciseSet(ExerciseSet set) {
        if (set == null) {
            throw new IllegalArgumentException("ExerciseSet cannot be null");
        }
        this.sets.remove(set);
    }

    public void setExercise(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException("ExercisePlan must have a valid Exercise");
        }
        this.exercise = exercise;
    }

    public void setWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("ExercisePlan must have a valid Workout");
        }
        this.workout = workout;
    }
}
