package com.kressin.fitness_app.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kressin.fitness_app.exception.BusinessException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Workout {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 500)
    private String description;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercisePlan> exercisePlans = new ArrayList<>();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    protected Workout() {
    }

    public Workout(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Workout name cannot be empty");
        }
        if (description == null) {
            throw new BusinessException("Workout description cannot be null");
        }
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ExercisePlan> getExercisePlans() {
        return Collections.unmodifiableList(exercisePlans);
    }

    public void addExercisePlan(ExercisePlan plan) {
        if (plan == null) {
            throw new BusinessException("Not possible to add null ExercisePlan to Workout");
        }

        this.exercisePlans.add(plan);
    }

    public void removeExercisePlan(ExercisePlan plan) {
        if (plan == null) {
            throw new BusinessException("Not possible to remove null ExercisePlan from Workout");
        }
        if (!this.exercisePlans.remove(plan)) {
            throw new BusinessException(
                    "Why are we trying to delete an exercise plan not present in the workout?");
        }
    }

    public List<WorkoutPlan> getWorkoutPlans() {
        return Collections.unmodifiableList(workoutPlans);
    }

    public void addWorkoutPlan(WorkoutPlan plan) {
        if (plan == null) {
            throw new BusinessException("Not possible to add null WorkoutPlan to Workout");
        }
        workoutPlans.add(plan);
    }

    public void removeWorkoutPlan(WorkoutPlan plan) {
        if (plan == null) {
            throw new BusinessException("Not possible to remove null WorkoutPlan from Workout");
        }
        if (!workoutPlans.remove(plan)) {
            throw new BusinessException(
                    "Why are we trying to delete an WorkoutPlan not present in the Workout?");
        }
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Workout name cannot be empty");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new BusinessException("Workout description cannot be null");
        }
        this.description = description;
    }
}
