package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.repository.ScheduleEntryRepository;

@Service
public class ScheduleEntryService {
  private final ScheduleEntryRepository repository;

  public ScheduleEntryService(ScheduleEntryRepository repository) {
    this.repository = repository;
  }

  public List<ScheduleEntry> getAllPlans() {
    return repository.findAll();
  }
}