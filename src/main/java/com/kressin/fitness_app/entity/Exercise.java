package com.kressin.fitness_app.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Exercise {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private String category;
    private String muscleGroup;

    @OneToMany(mappedBy = "exercise")
    private List<ExercisePlan> exercisePlans = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public long getId() {
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
        return exercisePlans;
    }
}
