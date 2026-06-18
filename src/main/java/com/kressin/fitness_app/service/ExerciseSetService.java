package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.entity.ExerciseSet;
import com.kressin.fitness_app.repository.ExerciseSetRepository;

@Service
public class ExerciseSetService {
  private final ExerciseSetRepository repository;

  public ExerciseSetService(ExerciseSetRepository repository) {
    this.repository = repository;
  }

  public List<ExerciseSet> getAllSets() {
    return repository.findAll();
  }
}
