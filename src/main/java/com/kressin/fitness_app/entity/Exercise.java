package com.kressin.fitness_app.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Exercise {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String category;
    private String muscleGroup;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercisePlan> exercisePlans = new ArrayList<>();

    protected Exercise() {
    }

    public Exercise(String name, String description, String category, String muscleGroup) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Exercise name cannot be empty");
        }
        if (description == null) {
            throw new IllegalArgumentException("Exercise description cannot be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("Exercise category cannot be null");
        }
        if (muscleGroup == null) {
            throw new IllegalArgumentException("Exercise muscleGroup cannot be null");
        }

        this.name = name;
        this.description = description;
        this.category = category;
        this.muscleGroup = muscleGroup;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Exercise name cannot be empty");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Exercise description cannot be null");
        }
        this.description = description;
    }

    public void setCategory(String category) {
        if (category == null) {
            throw new IllegalArgumentException("Exercise category cannot be null");
        }
        this.category = category;
    }

    public void setMuscleGroup(String muscleGroup) {
        if (muscleGroup == null) {
            throw new IllegalArgumentException("Exercise muscleGroup cannot be null");
        }
        this.muscleGroup = muscleGroup;
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

    public String getCategory() {
        return category;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public List<ExercisePlan> getExercisePlans() {
        return Collections.unmodifiableList(exercisePlans);
    }

    public void addExercisePlan(ExercisePlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Not possible to add null ExercisePlan to Exercise");
        }
        exercisePlans.add(plan);
    }

    public void removeExercisePlan(ExercisePlan plan) {
        if (plan == null) {
            throw new IllegalArgumentException("Not possible to remove null ExercisePlan from Exercise");
        }
        if (!this.exercisePlans.remove(plan)) {
            throw new IllegalArgumentException(
                    "Why are we trying to delete an exercise plan not present in the Exercise?");
        }
    }
}
