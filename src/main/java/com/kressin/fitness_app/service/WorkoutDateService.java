package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutDateResponse;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.mapper.ScheduleEntryMapper;
import com.kressin.fitness_app.mapper.WorkoutDateMapper;
import com.kressin.fitness_app.repository.WorkoutDateRepository;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutDateCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutDateCommand;

@Service
public class WorkoutDateService {
    private final WorkoutDateRepository workoutDateRepo;
    private final ScheduleEntryService scheduleEntryService;

    public WorkoutDateService(WorkoutDateRepository workoutDateRepo, ScheduleEntryService scheduleEntryService) {
        this.workoutDateRepo = workoutDateRepo;
        this.scheduleEntryService = scheduleEntryService;
    }

    public WorkoutDateResponse addWorkoutDate(CreateWorkoutDateCommand command, WorkoutPlan workoutPlan) {
        WorkoutDate workoutDate = new WorkoutDate(workoutPlan, command.scheduleType());

        for (CreateScheduleEntryCommand scheduleEntryCommand : command.scheduleEntries()) {
            scheduleEntryService.addScheduleEntry(scheduleEntryCommand, workoutDate);
        }

        return WorkoutDateMapper.toResponse(workoutDate);
    }

    public WorkoutDateResponse updateWorkoutDate(UpdateWorkoutDateCommand command) {
        WorkoutDate workoutDate = workoutDateRepo.getReferenceById(command.id());

        if (command.scheduleType() != null) {
            workoutDate.setScheduleType(command.scheduleType());
        }

        if (command.scheduleEntries() != null) {
            for (UpdateScheduleEntryCommand updateScheduleEntryCommand : command.scheduleEntries()) {
                if (updateScheduleEntryCommand.id() != null) {
                    scheduleEntryService.updateScheduleEntry(updateScheduleEntryCommand);
                } else {
                    CreateScheduleEntryCommand createCommand = ScheduleEntryMapper
                            .toCreateCommandFromUpdate(updateScheduleEntryCommand);
                    scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
                }
            }
        }

        return WorkoutDateMapper.toResponse(workoutDate);
    }

    public WorkoutDateResponse getWorkoutDate(Long id) {
        return WorkoutDateMapper.toResponse(workoutDateRepo.getReferenceById(id));
    }

    public List<WorkoutDateResponse> getAllWorkoutDates() {
        return WorkoutDateMapper.toResponseList(workoutDateRepo.findAll());
    }
}
