package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.repository.WorkoutDateRepository;

@Service
public class WorkoutDateService {
  private final WorkoutDateRepository repository;

  public WorkoutDateService(WorkoutDateRepository repository) {
    this.repository = repository;
  }

  public List<WorkoutDate> getAllPlans() {
    return repository.findAll();
  }
}