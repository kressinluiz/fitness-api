package com.kressin.fitness_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.exception.ExerciseNotFoundException;
import com.kressin.fitness_app.mapper.ExerciseMapper;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.service.command.CreateExerciseCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseCommand;

import jakarta.transaction.Transactional;

@Service
public class ExerciseService {
    private final ExerciseRepository repository;

    public ExerciseService(ExerciseRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ExerciseResponse addExercise(CreateExerciseCommand command) {
        Exercise exercise = new Exercise(
                command.name(),
                command.description(),
                command.category(),
                command.muscleGroup());

        return ExerciseMapper.toResponse(repository.save(exercise));
    }

    @Transactional
    public ExerciseResponse updateExercise(UpdateExerciseCommand command) {
        Exercise exercise = repository.findById(command.id())
                .orElseThrow(() -> new ExerciseNotFoundException(command.id()));
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

    @Transactional
    public void deleteExercise(Long id) {
        if (!repository.existsById(id)) {
            throw new ExerciseNotFoundException(id);
        }
        repository.deleteById(id);
    }

    public ExerciseResponse getExercise(Long id) {
        return ExerciseMapper.toResponse(repository.findById(id).orElseThrow(() -> new ExerciseNotFoundException(id)));
    }

    public Page<ExerciseResponse> getAllExercises(Pageable pageable, String search) {
        Page<Exercise> page;

        if (search == null || search.isBlank()) {
            page = repository.findAll(pageable);
        } else {
            page = repository.findByNameContainingIgnoreCase(search, pageable);
        }

        return page.map(ExerciseMapper::toResponse);
    }
}
