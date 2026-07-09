package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ExerciseSetResponse;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.ExerciseSet;
import com.kressin.fitness_app.exception.ExerciseSetNotFoundException;
import com.kressin.fitness_app.mapper.ExerciseSetMapper;
import com.kressin.fitness_app.repository.ExerciseSetRepository;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;

import jakarta.transaction.Transactional;

@Service
public class ExerciseSetService {
    private final ExerciseSetRepository exerciseSetRepo;

    public ExerciseSetService(ExerciseSetRepository exerciseSetRepo) {
        this.exerciseSetRepo = exerciseSetRepo;
    }

    @Transactional
    public ExerciseSetResponse addExerciseSet(CreateExerciseSetCommand command, ExercisePlan plan) {
        ExerciseSet set = new ExerciseSet(
                command.reps(),
                command.weight(),
                plan);

        exerciseSetRepo.save(set);
        plan.addExerciseSet(set);
        return ExerciseSetMapper.toResponse(set);
    }

    @Transactional
    public ExerciseSetResponse updateExerciseSet(UpdateExerciseSetCommand command) {
        if (command.shouldDelete() != null && command.shouldDelete()) {
            deleteExerciseSet(command.id());
            return null;
        }

        ExerciseSet set = exerciseSetRepo.findById(command.id())
                .orElseThrow(() -> new ExerciseSetNotFoundException(command.id()));
        if (command.reps() != null) {
            set.setReps(command.reps());
        }

        if (command.weight() != null) {
            set.setWeight(command.weight());
        }

        return ExerciseSetMapper.toResponse(set);
    }

    @Transactional
    public void deleteExerciseSet(Long id) {
        ExerciseSet set = exerciseSetRepo.findById(id).orElseThrow(() -> new ExerciseSetNotFoundException(id));
        set.getExercisePlan().removeExerciseSet(set);
        exerciseSetRepo.deleteById(id);
    }

    public ExerciseSetResponse getExerciseSet(Long id) {
        return ExerciseSetMapper
                .toResponse(exerciseSetRepo.findById(id).orElseThrow(() -> new ExerciseSetNotFoundException(id)));
    }

    public List<ExerciseSetResponse> getAllExerciseSets() {
        return ExerciseSetMapper.toResponseList(exerciseSetRepo.findAll());
    }
}
