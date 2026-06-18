package com.kressin.fitness_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_app.entity.WorkoutPlan;

public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {}
