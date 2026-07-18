package com.kressin.fitness_app.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.UpdateExercisePlansOrderCommand;
import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.exception.BusinessException;
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

    public WorkoutService(WorkoutRepository workoutRepo, ExercisePlanService exercisePlanService) {
        this.workoutRepo = workoutRepo;
        this.exercisePlanService = exercisePlanService;
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

    public Page<WorkoutResponse> getAllWorkouts(Pageable pageable, String search) {
        Page<Workout> page;

        if (search == null || search.isBlank()) {
            page = workoutRepo.findAll(pageable);
        } else {
            page = workoutRepo.findByNameContainingIgnoreCase(search, pageable);
        }

        return page.map(WorkoutMapper::toResponse);
    }

    public WorkoutResponse getWorkout(Long id) {
        return WorkoutMapper.toResponse(workoutRepo.findById(id).orElseThrow(() -> new WorkoutNotFoundException(id)));
    }

    @Transactional
    public void deleteWorkout(Long id) {
        Workout workout = workoutRepo.findById(id).orElseThrow(() -> new WorkoutNotFoundException(id));
        workoutRepo.deleteById(workout.getId());
    }

    @Transactional
    public WorkoutResponse updateWorkoutExercisePlansOrder(UpdateExercisePlansOrderCommand command) {
        Workout workout = workoutRepo.findById(command.workoutId())
                .orElseThrow(() -> new WorkoutNotFoundException(command.workoutId()));

        List<ExercisePlan> exercisePlans = workout.getExercisePlans();

        if (command.exercisePlanIds().size() != exercisePlans.size()) {
            throw new BusinessException("The request must contain all exercise plans.");
        }

        Set<Long> uniqueIds = new HashSet<>(command.exercisePlanIds());

        if (uniqueIds.size() != command.exercisePlanIds().size()) {
            throw new BusinessException("Duplicate exercise plan ids.");
        }

        Set<Long> existingIds = exercisePlans.stream().map(ExercisePlan::getId).collect(Collectors.toSet());
        if (!existingIds.containsAll(command.exercisePlanIds())) {
            throw new BusinessException("One or more exercise plans do not belong to the workout.");
        }

        Map<Long, ExercisePlan> plansById = exercisePlans.stream()
                .collect(Collectors.toMap(ExercisePlan::getId, Function.identity()));

        for (int i = 0; i < command.exercisePlanIds().size(); i++) {
            Long id = command.exercisePlanIds().get(i);
            ExercisePlan plan = plansById.get(id);
            plan.setPosition(i + 1);
        }

        return WorkoutMapper.toResponse(workout);
    }
}
