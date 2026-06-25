package com.kressin.fitness_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kressin.fitness_app.entity.ScheduleEntry;

public interface ScheduleEntryRepository extends JpaRepository<ScheduleEntry, Long> {}

