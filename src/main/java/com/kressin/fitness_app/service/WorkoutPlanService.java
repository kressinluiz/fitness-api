package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;

@Service
public class WorkoutPlanService {
  private final WorkoutPlanRepository repository;

  public WorkoutPlanService(WorkoutPlanRepository repository) {
    this.repository = repository;
  }

  public List<WorkoutPlan> getAllPlans() {
    return repository.findAll();
  }
}