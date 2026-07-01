package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.mapper.ExercisePlanMapper;
import com.kressin.fitness_app.mapper.WorkoutMapper;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutCommand;

@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepo;
    private final ExercisePlanService exercisePlanService;

    public WorkoutService(WorkoutRepository workoutRepo, ExercisePlanService exercisePlanService) {
        this.workoutRepo = workoutRepo;
        this.exercisePlanService = exercisePlanService;
    }

    public WorkoutResponse addWorkout(CreateWorkoutCommand command) {
        Workout workout = new Workout(command.name(), command.description());
        workout = workoutRepo.save(workout);
        for (CreateExercisePlanCommand createExercisePlanCommand : command.exercisePlans()) {
            exercisePlanService.addExercisePlan(createExercisePlanCommand, workout);
        }

        return WorkoutMapper.toResponse(workout);
    }

    public WorkoutResponse updateWorkout(UpdateWorkoutCommand command) {
        Workout workout = workoutRepo.getReferenceById(command.id()); // if this fails what happens?

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
        return WorkoutMapper.toResponse(workoutRepo.getReferenceById(id));
    }

    public void deleteWorkout(Long id) {
        workoutRepo.deleteById(id);
    }
}
