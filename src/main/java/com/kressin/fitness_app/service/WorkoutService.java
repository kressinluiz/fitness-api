package com.kressin.fitness_app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.exception.WorkoutNotFoundException;
import com.kressin.fitness_app.mapper.ExercisePlanMapper;
import com.kressin.fitness_app.mapper.WorkoutMapper;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutCommand;

import jakarta.transaction.Transactional;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepo;
    private final ExercisePlanService exercisePlanService;
    private final WorkoutPlanService workoutPlanService;

    public WorkoutService(WorkoutRepository workoutRepo, ExercisePlanService exercisePlanService,
            WorkoutPlanService workoutPlanService) {
        this.workoutRepo = workoutRepo;
        this.exercisePlanService = exercisePlanService;
        this.workoutPlanService = workoutPlanService;
    }

    @Transactional
    public WorkoutResponse addWorkout(CreateWorkoutCommand command) {
        Workout workout = new Workout(command.name(), command.description());
        workout = workoutRepo.save(workout);
        if (command.exercisePlans() != null) {
            for (CreateExercisePlanCommand createExercisePlanCommand : command.exercisePlans()) {
                exercisePlanService.addExercisePlan(createExercisePlanCommand, workout);
            }
        }

        return WorkoutMapper.toResponse(workout);
    }

    @Transactional
    public WorkoutResponse updateWorkout(UpdateWorkoutCommand command) {
        Workout workout = workoutRepo.findById(command.id())
                .orElseThrow(() -> new WorkoutNotFoundException(command.id()));

        if (command.name() != null) {
            workout.setName(command.name());
        }

        if (command.description() != null) {
            workout.setDescription(command.description());
        }

        if (command.exercisePlans() != null) {
            for (UpdateExercisePlanCommand planCommand : command.exercisePlans()) {
                if (planCommand.id() != null) {
                    exercisePlanService.updateExercisePlan(planCommand);
                } else {
                    CreateExercisePlanCommand createCommand = ExercisePlanMapper
                            .toCreateCommandFromUpdateCommand(planCommand);
                    exercisePlanService.addExercisePlan(createCommand, workout);
                }
            }
        }

        return WorkoutMapper.toResponse(workout);
    }

    public List<WorkoutResponse> getAllWorkouts() {
        return WorkoutMapper.toResponseList(workoutRepo.findAll());
    }

    public WorkoutResponse getWorkout(Long id) {
        return WorkoutMapper.toResponse(workoutRepo.findById(id).orElseThrow(() -> new WorkoutNotFoundException(id)));
    }

    @Transactional
    public void deleteWorkout(Long id) {
        Workout workout = workoutRepo.findById(id).orElseThrow(() -> new WorkoutNotFoundException(id));
        if (workout.getExercisePlans() != null) {
            for (ExercisePlan plan : new ArrayList<>(workout.getExercisePlans())) {
                exercisePlanService.deleteExercisePlan(plan.getId());
            }
        }
        if (workout.getWorkoutPlans() != null) {
            for (WorkoutPlan plan : new ArrayList<>(workout.getWorkoutPlans())) {
                workoutPlanService.deleteWorkoutPlan(plan.getId());
            }
        }

        workoutRepo.deleteById(id);
    }
}
