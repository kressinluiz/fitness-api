package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.repository.ExercisePlanRepository;

@Service
public class ExercisePlanService {
  private final ExercisePlanRepository repository;

  public ExercisePlanService(ExercisePlanRepository repository) {
    this.repository = repository;
  }

  public List<ExercisePlan> getAllPlans() {
    return repository.findAll();
  }
}
