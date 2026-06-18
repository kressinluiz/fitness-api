package com.kressin.fitness_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_app.entity.ExercisePlan;

public interface ExercisePlanRepository
    extends JpaRepository<ExercisePlan, Long> {

}
