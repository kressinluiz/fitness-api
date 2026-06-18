package com.kressin.fitness_app.entity;

import java.util.ArrayList;
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
  private long id;

  @ManyToOne
  @JoinColumn(name = "exercise_id")
  private Exercise exercise;

  @OneToMany(mappedBy = "exercisePlan", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ExerciseSet> sets = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "workout_id")
  private Workout workout;

  public long getId() {
    return id;
  }

  public Exercise getExercise() {
    return exercise;
  }

  public List<ExerciseSet> getSets() {
    return sets;
  }

  public void setExercise(Exercise exercise) {
    this.exercise = exercise;
  }

  public void setWorkout(Workout workout) {
    this.workout = workout;
  }
}
