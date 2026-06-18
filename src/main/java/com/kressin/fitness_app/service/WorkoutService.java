package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.ExerciseSet;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.mapper.WorkoutMapper;
import com.kressin.fitness_app.repository.ExercisePlanRepository;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.repository.ExerciseSetRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutCommand;

@Service
public class WorkoutService {
  private final WorkoutRepository workoutRepo;
  private final ExerciseRepository exerciseRepo;
  private final ExercisePlanRepository exercisePlanRepo;
  private final ExerciseSetRepository exerciseSetRepo;

  public WorkoutService(WorkoutRepository workoutRepo, ExerciseRepository exerciseRepo,
      ExercisePlanRepository exercisePlanRepo, ExerciseSetRepository exerciseSetRepo) {
    this.workoutRepo = workoutRepo;
    this.exerciseRepo = exerciseRepo;
    this.exercisePlanRepo = exercisePlanRepo;
    this.exerciseSetRepo = exerciseSetRepo;
  }

  public WorkoutResponse addWorkout(CreateWorkoutCommand command) {
    Workout workout = new Workout();
    workout.setName(command.name());
    workout.setDescription(command.description());

    for (CreateExercisePlanCommand planCommand : command.exercisePlans()) {
      Exercise exercise = exerciseRepo.getReferenceById(planCommand.exerciseId()); // if this fails what happens?

      ExercisePlan plan = new ExercisePlan();
      plan.setExercise(exercise);
      plan.setWorkout(workout);

      exercise.getExercisePlans().add(plan);

      for (CreateExerciseSetCommand setCommand : planCommand.sets()) {
        ExerciseSet set = new ExerciseSet();
        set.setReps(setCommand.reps());
        set.setWeight(setCommand.weight());
        set.setExercisePlan(plan);
        plan.getSets().add(set);
      }

      workout.getExercisePlans().add(plan);
    }

    return WorkoutMapper.toResponse(workoutRepo.save(workout));
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
        Exercise exercise = exerciseRepo.getReferenceById(planCommand.exerciseId());
        if (planCommand.id() != null) {
          ExercisePlan plan = exercisePlanRepo.getReferenceById(planCommand.id());
          if (planCommand.exerciseId() != null) {
            plan.getExercise().getExercisePlans().remove(plan);

            plan.setExercise(exercise);
            exercise.getExercisePlans().add(plan);
          }

          if (planCommand.sets() != null) {
            for (UpdateExerciseSetCommand setCommand : planCommand.sets()) {
              if (setCommand.id() != null) {
                ExerciseSet set = exerciseSetRepo.getReferenceById(setCommand.id());
                if (setCommand.reps() != null) {
                  set.setReps(setCommand.reps());
                }

                if (setCommand.weight() != null) {
                  set.setWeight(setCommand.weight());
                }
              } else {
                ExerciseSet set = new ExerciseSet();
                set.setReps(setCommand.reps());
                set.setWeight(setCommand.weight());
                set.setExercisePlan(plan);
                plan.getSets().add(set);
              }
            }
          }
        } else {
          ExercisePlan plan = new ExercisePlan();
          plan.setExercise(exercise);
          plan.setWorkout(workout);

          exercise.getExercisePlans().add(plan);

          for (UpdateExerciseSetCommand setCommand : planCommand.sets()) {
            ExerciseSet set = new ExerciseSet();
            set.setReps(setCommand.reps());
            set.setWeight(setCommand.weight());
            set.setExercisePlan(plan);
            plan.getSets().add(set);
          }

          workout.getExercisePlans().add(plan);
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
