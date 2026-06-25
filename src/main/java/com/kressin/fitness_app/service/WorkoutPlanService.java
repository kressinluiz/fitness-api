package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.mapper.WorkoutPlanMapper;
import com.kressin.fitness_app.repository.ScheduleEntryRepository;
import com.kressin.fitness_app.repository.WorkoutDateRepository;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutPlanCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutPlanCommand;

@Service
public class WorkoutPlanService {
    private final WorkoutPlanRepository workoutPlanRepo;
    private final WorkoutRepository workoutRepo;
    private final WorkoutDateRepository workoutDateRepo;
    private final ScheduleEntryRepository scheduleEntryRepo;

    public WorkoutPlanService(WorkoutPlanRepository workoutPlanRepo, WorkoutRepository workoutRepo, WorkoutDateRepository workoutDateRepo, ScheduleEntryRepository scheduleEntryRepository) {
        this.workoutPlanRepo = workoutPlanRepo;
        this.workoutRepo = workoutRepo;
        this.workoutDateRepo = workoutDateRepo;
        this.scheduleEntryRepo = scheduleEntryRepository;
    }

    public WorkoutPlanResponse addWorkoutPlan(CreateWorkoutPlanCommand command) {

        WorkoutPlan workoutPlan = new WorkoutPlan();
        Workout workout = workoutRepo.getReferenceById(command.workoutId());
        WorkoutDate workoutDate = new WorkoutDate();

        for (CreateScheduleEntryCommand scheduleEntryCommand : command.workoutDate().scheduleEntries()) {
            ScheduleEntry scheduleEntry = new ScheduleEntry();
            scheduleEntry.setWorkoutDate(workoutDate);
            scheduleEntry.setWeekDay(scheduleEntryCommand.weekDay());
            scheduleEntry.setDateTime(scheduleEntryCommand.dateTime());
            workoutDate.getScheduleEntries().add(scheduleEntry);
        }

        workoutDate.setScheduleType(command.workoutDate().scheduleType());
        workoutDate.setWorkoutPlan(workoutPlan);

        workout.getWorkoutPlans().add(workoutPlan);
        workoutPlan.setWorkout(workout);
        workoutPlan.setWorkoutDate(workoutDate);

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
            WorkoutDate workoutDate = workoutDateRepo.getReferenceById(command.workoutDate().id());

            if (command.workoutDate().scheduleType() != null) {
                workoutDate.setScheduleType(command.workoutDate().scheduleType());
            }

            if (command.workoutDate().scheduleEntries() != null) {
                for (UpdateScheduleEntryCommand updateScheduleEntryCommand : command.workoutDate().scheduleEntries()) {
                    if (updateScheduleEntryCommand.id() != null) {
                        ScheduleEntry scheduleEntry = scheduleEntryRepo.getReferenceById(updateScheduleEntryCommand.id());
                        if (updateScheduleEntryCommand.weekDay() != null) {
                            scheduleEntry.setWeekDay(updateScheduleEntryCommand.weekDay());
                        }
                        
                        if (updateScheduleEntryCommand.dateTime() != null) {
                            scheduleEntry.setDateTime(updateScheduleEntryCommand.dateTime());
                        }
                    } else {
                        ScheduleEntry scheduleEntry = new ScheduleEntry();
                        scheduleEntry.setWorkoutDate(workoutDate);
                        scheduleEntry.setWeekDay(updateScheduleEntryCommand.weekDay());
                        scheduleEntry.setDateTime(updateScheduleEntryCommand.dateTime());
                        workoutDate.getScheduleEntries().add(scheduleEntry);
                    }
                }
            }
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
