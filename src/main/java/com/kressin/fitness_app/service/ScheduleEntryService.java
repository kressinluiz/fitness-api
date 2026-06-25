package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ScheduleEntryResponse;
import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.mapper.ScheduleEntryMapper;
import com.kressin.fitness_app.repository.ScheduleEntryRepository;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;

@Service
public class ScheduleEntryService {
    private final ScheduleEntryRepository scheduleEntryRepo;

    public ScheduleEntryService(ScheduleEntryRepository scheduleEntryRepo) {
        this.scheduleEntryRepo = scheduleEntryRepo;
    }

    public ScheduleEntryResponse addScheduleEntry(CreateScheduleEntryCommand command, WorkoutDate workoutDate) {
        ScheduleEntry scheduleEntry = new ScheduleEntry();
        scheduleEntry.setWeekDay(command.weekDay());
        scheduleEntry.setDateTime(command.dateTime());
        scheduleEntry.setWorkoutDate(workoutDate);
        workoutDate.getScheduleEntries().add(scheduleEntry);
        return ScheduleEntryMapper.toResponse(scheduleEntry);
    }

    public ScheduleEntryResponse updateScheduleEntry(UpdateScheduleEntryCommand command) {
        ScheduleEntry scheduleEntry = scheduleEntryRepo.getReferenceById(command.id());
        if (command.weekDay() != null) {
            scheduleEntry.setWeekDay(command.weekDay());
        }

        if (command.dateTime() != null) {
            scheduleEntry.setDateTime(command.dateTime());
        }

        return ScheduleEntryMapper.toResponse(scheduleEntry);
    }

    public ScheduleEntryResponse getScheduleEntry(Long id) {
        return ScheduleEntryMapper.toResponse(scheduleEntryRepo.getReferenceById(id));
    }

    public List<ScheduleEntryResponse> getAllScheduleEntries() {
        return ScheduleEntryMapper.toResponseList(scheduleEntryRepo.findAll());
    }
}
