package com.kressin.fitness_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_app.entity.WorkoutDate;

public interface WorkoutDateRepository extends JpaRepository<WorkoutDate, Long> {}

