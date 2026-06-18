package com.kressin.fitness_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_app.entity.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {}
