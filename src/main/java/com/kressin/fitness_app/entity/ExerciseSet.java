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
  private long id;
  private Integer reps;
  private Double weight;

  @ManyToOne
  @JoinColumn(name = "exerciseplan_id")
  private ExercisePlan exercisePlan;

  public long getId() {
    return id;
  }

  public void setReps(Integer reps) {
    this.reps = reps;
  }

  public void setWeight(Double weight) {
    this.weight = weight;
  }

  public void setExercisePlan(ExercisePlan exercisePlan) {
      this.exercisePlan = exercisePlan;
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
}
