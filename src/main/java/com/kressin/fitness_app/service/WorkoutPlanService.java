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

import jakarta.transaction.Transactional;

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

    @Transactional
    public WorkoutPlanResponse addWorkoutPlan(CreateWorkoutPlanCommand command) {
        if (command.workoutId() == null || !workoutRepo.existsById(command.workoutId())) {
            throw new IllegalArgumentException("Workout ID must be valid");
        }
        if (command.workoutDate() == null) {
            throw new IllegalArgumentException("WorkoutDate create command must be valid");
        }

        Workout workout = workoutRepo.getReferenceById(command.workoutId());
        WorkoutPlan workoutPlan = new WorkoutPlan(workout);
        workoutDateService.addWorkoutDate(command.workoutDate(), workoutPlan);

        return WorkoutPlanMapper.toResponse(workoutPlanRepo.save(workoutPlan));
    }

    @Transactional
    public WorkoutPlanResponse updateWorkoutPlan(UpdateWorkoutPlanCommand command) {
        if (command.id() == null || !workoutPlanRepo.existsById(command.id())) {
            throw new IllegalArgumentException("WorkoutPlan ID must be valid");
        }
        WorkoutPlan workoutPlan = workoutPlanRepo.getReferenceById(command.id());

        if (command.workoutId() != null) {
            workoutPlan.getWorkout().removeWorkoutPlan(workoutPlan);
            Workout workout = workoutRepo.getReferenceById(command.workoutId());
            workoutPlan.setWorkout(workout);
        }

        if (command.workoutDate() != null) {
            workoutDateService.updateWorkoutDate(command.workoutDate());
        }

        return WorkoutPlanMapper.toResponse(workoutPlan);
    }

    @Transactional
    public WorkoutPlanResponse getWorkoutPlan(Long id) {
        if (id == null || !workoutPlanRepo.existsById(id)) {
            throw new IllegalArgumentException("WorkoutPlan ID must be valid");
        }
        return WorkoutPlanMapper.toResponse(workoutPlanRepo.getReferenceById(id));
    }

    @Transactional
    public List<WorkoutPlanResponse> getAllWorkoutPlans() {
        return WorkoutPlanMapper.toResponseList(workoutPlanRepo.findAll());
    }

    public void deleteWorkoutPlan(Long id) {
        if (id == null || !workoutPlanRepo.existsById(id)) {
            throw new IllegalArgumentException("WorkoutPlan ID must be valid");
        }
        workoutPlanRepo.deleteById(id);
    }
}
