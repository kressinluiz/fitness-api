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

import jakarta.transaction.Transactional;

@Service
public class WorkoutDateService {
    private final WorkoutDateRepository workoutDateRepo;
    private final ScheduleEntryService scheduleEntryService;

    public WorkoutDateService(WorkoutDateRepository workoutDateRepo, ScheduleEntryService scheduleEntryService) {
        this.workoutDateRepo = workoutDateRepo;
        this.scheduleEntryService = scheduleEntryService;
    }

    @Transactional
    public WorkoutDateResponse addWorkoutDate(CreateWorkoutDateCommand command, WorkoutPlan workoutPlan) {
        if (command.scheduleType() == null) {
            throw new IllegalArgumentException("Create command must have a valid ScheduleType");
        }
        WorkoutDate workoutDate = new WorkoutDate(workoutPlan, command.scheduleType());
        workoutDate = workoutDateRepo.save(workoutDate);
        workoutPlan.setWorkoutDate(workoutDate);

        if (command.scheduleEntries() != null) {
            for (CreateScheduleEntryCommand scheduleEntryCommand : command.scheduleEntries()) {
                scheduleEntryService.addScheduleEntry(scheduleEntryCommand, workoutDate);
            }
        }

        return WorkoutDateMapper.toResponse(workoutDate);
    }

    @Transactional
    public WorkoutDateResponse updateWorkoutDate(UpdateWorkoutDateCommand command) {
        if (command.id() == null || !workoutDateRepo.existsById(command.id())) {
            throw new IllegalArgumentException("WorkoutDate ID must be valid");
        }

        WorkoutDate workoutDate = workoutDateRepo.getReferenceById(command.id());

        if (command.scheduleType() != null) {
            workoutDate.setScheduleType(command.scheduleType());
        }

        if (command.scheduleEntries() != null) {
            for (UpdateScheduleEntryCommand updateScheduleEntryCommand : command.scheduleEntries()) {
                if (updateScheduleEntryCommand.id() != null) {
                    scheduleEntryService.updateScheduleEntry(updateScheduleEntryCommand);
                } else {
                    if (updateScheduleEntryCommand.weekDay() == null || updateScheduleEntryCommand.dateTime() == null) {
                        throw new IllegalArgumentException("Create Command must have valid weekDay and dateTime");
                    }
                    CreateScheduleEntryCommand createCommand = ScheduleEntryMapper
                            .toCreateCommandFromUpdate(updateScheduleEntryCommand);
                    scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
                }
            }
        }

        return WorkoutDateMapper.toResponse(workoutDate);
    }

    @Transactional
    public WorkoutDateResponse getWorkoutDate(Long id) {
        if (id == null || !workoutDateRepo.existsById(id)) {
            throw new IllegalArgumentException("WorkoutDate ID must be valid");
        }
        return WorkoutDateMapper.toResponse(workoutDateRepo.getReferenceById(id));
    }

    @Transactional
    public List<WorkoutDateResponse> getAllWorkoutDates() {
        return WorkoutDateMapper.toResponseList(workoutDateRepo.findAll());
    }
}
