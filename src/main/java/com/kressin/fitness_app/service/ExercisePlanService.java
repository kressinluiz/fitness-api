package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ExercisePlanResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.exception.ExercisePlanNotFoundException;
import com.kressin.fitness_app.mapper.ExercisePlanMapper;
import com.kressin.fitness_app.mapper.ExerciseSetMapper;
import com.kressin.fitness_app.repository.ExercisePlanRepository;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;

import jakarta.transaction.Transactional;

@Service
public class ExercisePlanService {
    private final ExercisePlanRepository exercisePlanRepo;
    private final ExerciseRepository exerciseRepo;
    private final ExerciseSetService exerciseSetService;

    public ExercisePlanService(ExercisePlanRepository exercisePlanRepo, ExerciseRepository exerciseRepo,
            ExerciseSetService exerciseSetService) {
        this.exercisePlanRepo = exercisePlanRepo;
        this.exerciseRepo = exerciseRepo;
        this.exerciseSetService = exerciseSetService;
    }

    @Transactional
    public ExercisePlanResponse addExercisePlan(CreateExercisePlanCommand command, Workout workout) {
        Exercise exercise = exerciseRepo.findById(command.exerciseId())
                .orElseThrow(() -> new BusinessException("Exercise ID must be valid"));

        ExercisePlan plan = exercisePlanRepo.save(new ExercisePlan(exercise, workout));

        if (command.sets() != null) {
            for (CreateExerciseSetCommand createExerciseSetCommand : command.sets()) {
                exerciseSetService.addExerciseSet(createExerciseSetCommand, plan);
            }
        }

        workout.addExercisePlan(plan);
        exercise.addExercisePlan(plan);
        return ExercisePlanMapper.toResponse(plan);
    }

    @Transactional
    public ExercisePlanResponse updateExercisePlan(UpdateExercisePlanCommand command) {
        if (command.shouldDelete() != null && command.shouldDelete()) {
            deleteExercisePlan(command.id());
            return null;
        }

        ExercisePlan plan = exercisePlanRepo.findById(command.id())
                .orElseThrow(() -> new ExercisePlanNotFoundException(command.id()));

        if (command.exerciseId() != null && command.exerciseId() != plan.getExercise().getId()) {
            plan.getExercise().removeExercisePlan(plan);
            Exercise exercise = exerciseRepo.findById(command.exerciseId())
                    .orElseThrow(() -> new BusinessException("Exercise ID must be valid"));
            plan.setExercise(exercise);
            exercise.addExercisePlan(plan);
        }

        if (command.sets() != null) {
            for (UpdateExerciseSetCommand setCommand : command.sets()) {
                if (setCommand.id() != null) {
                    exerciseSetService.updateExerciseSet(setCommand);
                } else {
                    exerciseSetService.addExerciseSet(ExerciseSetMapper.toCreateCommandFromUpdateCommand(setCommand),
                            plan);
                }
            }
        }

        return ExercisePlanMapper.toResponse(plan);
    }

    @Transactional
    public void deleteExercisePlan(Long id) {
        ExercisePlan plan = exercisePlanRepo.findById(id).orElseThrow(() -> new ExercisePlanNotFoundException(id));
        plan.getExercise().removeExercisePlan(plan);
        plan.getWorkout().removeExercisePlan(plan);
        exercisePlanRepo.deleteById(id);
    }

    public ExercisePlanResponse getExercisePlan(Long id) {
        ExercisePlan plan = exercisePlanRepo.findById(id).orElseThrow(() -> new ExercisePlanNotFoundException(id));
        return ExercisePlanMapper.toResponse(plan);
    }

    public List<ExercisePlanResponse> getAllExercisePlans() {
        return ExercisePlanMapper.toResponseList(exercisePlanRepo.findAll());
    }
}
