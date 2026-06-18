package com.kressin.fitness_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_app.entity.ExerciseSet;

public interface ExerciseSetRepository
    extends JpaRepository<ExerciseSet, Long> {

}
