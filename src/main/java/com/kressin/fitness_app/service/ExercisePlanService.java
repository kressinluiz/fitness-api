package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ExercisePlanResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.mapper.ExercisePlanMapper;
import com.kressin.fitness_app.mapper.ExerciseSetMapper;
import com.kressin.fitness_app.repository.ExercisePlanRepository;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;

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

    public ExercisePlanResponse addExercisePlan(CreateExercisePlanCommand command, Workout workout) {
        Exercise exercise = exerciseRepo.getReferenceById(command.exerciseId());
        ExercisePlan plan = new ExercisePlan();
        plan.setExercise(exercise);
        exercise.getExercisePlans().add(plan);
        plan.setWorkout(workout);
        workout.getExercisePlans().add(plan);

        for (CreateExerciseSetCommand createExerciseSetCommand : command.sets()) {
            exerciseSetService.addExerciseSet(createExerciseSetCommand, plan);
        }

        return ExercisePlanMapper.toResponse(plan);
    }

    public ExercisePlanResponse updateExercisePlan(UpdateExercisePlanCommand command) {
        ExercisePlan plan = exercisePlanRepo.getReferenceById(command.id());
        if (command.exerciseId() != null) {
            plan.getExercise().getExercisePlans().remove(plan);
            Exercise exercise = exerciseRepo.getReferenceById(command.exerciseId());
            plan.setExercise(exercise);
            exercise.getExercisePlans().add(plan);
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

    public ExercisePlanResponse getExercisePlan(Long id) {
        return ExercisePlanMapper.toResponse(exercisePlanRepo.getReferenceById(id));
    }

    public List<ExercisePlanResponse> getAllExercisePlans() {
        return ExercisePlanMapper.toResponseList(exercisePlanRepo.findAll());
    }
}
