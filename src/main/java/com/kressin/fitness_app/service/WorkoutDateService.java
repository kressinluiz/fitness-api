package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.WorkoutDateResponse;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.exception.WorkoutDateNotFoundException;
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
            throw new BusinessException("Create command must have a valid ScheduleType");
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
        WorkoutDate workoutDate = workoutDateRepo.findById(command.id())
                .orElseThrow(() -> new WorkoutDateNotFoundException(command.id()));

        if (command.scheduleType() != null) {
            workoutDate.setScheduleType(command.scheduleType());
        }

        if (command.scheduleEntries() != null) {
            for (UpdateScheduleEntryCommand updateScheduleEntryCommand : command.scheduleEntries()) {
                if (updateScheduleEntryCommand.id() != null) {
                    scheduleEntryService.updateScheduleEntry(updateScheduleEntryCommand);
                } else {
                    if (updateScheduleEntryCommand.weekDay() == null || updateScheduleEntryCommand.dateTime() == null) {
                        throw new BusinessException("Create Command must have valid weekDay and dateTime");
                    }
                    CreateScheduleEntryCommand createCommand = ScheduleEntryMapper
                            .toCreateCommandFromUpdate(updateScheduleEntryCommand);
                    scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
                }
            }
        }

        return WorkoutDateMapper.toResponse(workoutDate);
    }

    public WorkoutDateResponse getWorkoutDate(Long id) {
        WorkoutDate workoutDate = workoutDateRepo.findById(id).orElseThrow(() -> new WorkoutDateNotFoundException(id));
        return WorkoutDateMapper.toResponse(workoutDate);
    }

    // This @Transactional is hiding a lazyinitialization exception.
    // We need to improve this in the future to avoid N+1 queries and to avoid
    // lazyinitialization exceptions.
    // Let's have smaller DTOs and avoid returning the entire object graph.
    @Transactional
    public List<WorkoutDateResponse> getAllWorkoutDates() {
        return WorkoutDateMapper.toResponseList(workoutDateRepo.findAll());
    }
}
