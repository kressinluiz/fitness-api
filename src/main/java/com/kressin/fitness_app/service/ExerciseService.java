package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.exception.ExerciseNotFoundException;
import com.kressin.fitness_app.mapper.ExerciseMapper;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.service.command.CreateExerciseCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseCommand;

@Service
public class ExerciseService {
    private final ExerciseRepository repository;

    public ExerciseService(ExerciseRepository repository) {
        this.repository = repository;
    }

    public ExerciseResponse addExercise(CreateExerciseCommand command) {
        Exercise exercise = new Exercise(
                command.name(),
                command.description(),
                command.category(),
                command.muscleGroup());

        return ExerciseMapper.toResponse(repository.save(exercise));
    }

    public ExerciseResponse updateExercise(UpdateExerciseCommand command) {
        if (command.id() == null || !repository.existsById(command.id())) {
            throw new ExerciseNotFoundException(command.id());
        }
        Exercise exercise = repository.getReferenceById(command.id());
        if (command.name() != null) {
            exercise.setName(command.name());
        }
        if (command.description() != null) {
            exercise.setDescription(command.description());
        }
        if (command.category() != null) {
            exercise.setCategory(command.category());
        }
        if (command.muscleGroup() != null) {
            exercise.setMuscleGroup(command.muscleGroup());
        }

        return ExerciseMapper.toResponse(repository.save(exercise));
    }

    public void deleteExercise(Long id) {
        if (id == null || !repository.existsById(id)) {
            throw new ExerciseNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public ExerciseResponse getExercise(Long id) {
        if (id == null || !repository.existsById(id)) {
            throw new ExerciseNotFoundException(id);
        }
        return ExerciseMapper.toResponse(repository.getReferenceById(id));
    }

    public List<ExerciseResponse> getAllExercises() {
        return ExerciseMapper.toResponseList(repository.findAll());
    }
}
