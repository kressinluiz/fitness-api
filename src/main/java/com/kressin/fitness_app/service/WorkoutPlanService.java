package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.exception.WorkoutPlanNotFoundException;
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
        if (command.workoutDate() == null) {
            throw new BusinessException("WorkoutDate create command must be valid");
        }

        Workout workout = workoutRepo.findById(command.workoutId())
                .orElseThrow(() -> new BusinessException("Workout ID must be valid"));

        WorkoutPlan workoutPlan = new WorkoutPlan(workout);
        workoutDateService.addWorkoutDate(command.workoutDate(), workoutPlan);

        return WorkoutPlanMapper.toResponse(workoutPlanRepo.save(workoutPlan));
    }

    @Transactional
    public WorkoutPlanResponse updateWorkoutPlan(UpdateWorkoutPlanCommand command) {
        WorkoutPlan workoutPlan = workoutPlanRepo.findById(command.id())
                .orElseThrow(() -> new WorkoutPlanNotFoundException(command.id()));

        if (command.workoutId() != null) {
            workoutPlan.getWorkout().removeWorkoutPlan(workoutPlan);
            Workout workout = workoutRepo.findById(command.workoutId())
                    .orElseThrow(() -> new BusinessException("Workout ID must be valid"));
            workoutPlan.setWorkout(workout);
        }

        if (command.workoutDate() != null) {
            workoutDateService.updateWorkoutDate(command.workoutDate());
        }

        return WorkoutPlanMapper.toResponse(workoutPlan);
    }

    // This @Transactional is hiding a lazyinitialization exception.
    // We need to improve this in the future to avoid N+1 queries and to avoid
    // lazyinitialization exceptions.
    // Let's have smaller DTOs and avoid returning the entire object graph.
    @Transactional
    public WorkoutPlanResponse getWorkoutPlan(Long id) {
        WorkoutPlan workoutPlan = workoutPlanRepo.findById(id)
                .orElseThrow(() -> new WorkoutPlanNotFoundException(id));
        return WorkoutPlanMapper.toResponse(workoutPlan);
    }

    @Transactional
    public List<WorkoutPlanResponse> getAllWorkoutPlans() {
        return WorkoutPlanMapper.toResponseList(workoutPlanRepo.findAll());
    }

    @Transactional
    public void deleteWorkoutPlan(Long id) {
        if (id == null || !workoutPlanRepo.existsById(id)) {
            throw new WorkoutPlanNotFoundException(id);
        }
        workoutPlanRepo.deleteById(id);
    }
}
