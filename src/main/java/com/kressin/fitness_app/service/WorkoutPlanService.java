package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.mapper.WorkoutPlanMapper;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.command.CreateWorkoutPlanCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutPlanCommand;

@Service
public class WorkoutPlanService {
    private final WorkoutPlanRepository workoutPlanRepo;
    private final WorkoutRepository workoutRepo;
    private final WorkoutDateService workoutDateService;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepo, WorkoutRepository workoutRepo,
            WorkoutDateService workoutDateService) {
        this.workoutPlanRepo = workoutPlanRepo;
        this.workoutRepo = workoutRepo;
        this.workoutDateService = workoutDateService;
    }

    public WorkoutPlanResponse addWorkoutPlan(CreateWorkoutPlanCommand command) {
        WorkoutPlan workoutPlan = new WorkoutPlan();
        Workout workout = workoutRepo.getReferenceById(command.workoutId());
        workout.getWorkoutPlans().add(workoutPlan);
        workoutPlan.setWorkout(workout);
        workoutDateService.addWorkoutDate(command.workoutDate(), workoutPlan);

        return WorkoutPlanMapper.toResponse(workoutPlanRepo.save(workoutPlan));
    }

    public WorkoutPlanResponse updateWorkoutPlan(UpdateWorkoutPlanCommand command) {
        WorkoutPlan workoutPlan = workoutPlanRepo.getReferenceById(command.id());

        if (command.workoutId() != null) {
            workoutPlan.getWorkout().getWorkoutPlans().remove(workoutPlan);
            Workout workout = workoutRepo.getReferenceById(command.workoutId());
            workoutPlan.setWorkout(workout);
            workout.getWorkoutPlans().add(workoutPlan);
        }

        if (command.workoutDate() != null) {
            workoutDateService.updateWorkoutDate(command.workoutDate());
        }

        return WorkoutPlanMapper.toResponse(workoutPlan);
    }

    public WorkoutPlanResponse getWorkoutPlan(Long id) {
        return WorkoutPlanMapper.toResponse(workoutPlanRepo.getReferenceById(id));
    }

    public List<WorkoutPlanResponse> getWorkoutPlans() {
        return WorkoutPlanMapper.toResponseList(workoutPlanRepo.findAll());
    }

    public void deleteWorkoutPlan(Long id) {
        workoutPlanRepo.deleteById(id);
    }
}
