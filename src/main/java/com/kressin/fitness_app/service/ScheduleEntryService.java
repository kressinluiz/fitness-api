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
        if (command.weekDay() == null || command.dateTime() == null) {
            throw new IllegalArgumentException("Create Command must have valid weekDay and dateTime");
        }
        ScheduleEntry scheduleEntry = new ScheduleEntry(
                command.weekDay(),
                command.dateTime(),
                workoutDate);
        scheduleEntry = scheduleEntryRepo.save(scheduleEntry);
        workoutDate.addScheduleEntry(scheduleEntry);
        return ScheduleEntryMapper.toResponse(scheduleEntry);
    }

    public ScheduleEntryResponse updateScheduleEntry(UpdateScheduleEntryCommand command) {
        if (command.id() == null || !scheduleEntryRepo.existsById(command.id())) {
            throw new IllegalArgumentException("ScheduleEntry ID must be valid");
        }

        if (command.shouldDelete() != null && command.shouldDelete()) {
            deleteScheduleEntry(command.id());
        }

        ScheduleEntry scheduleEntry = scheduleEntryRepo.getReferenceById(command.id());

        if (command.weekDay() != null) {
            scheduleEntry.setWeekDay(command.weekDay());
        }

        if (command.dateTime() != null) {
            scheduleEntry.setDateTime(command.dateTime());
        }

        return ScheduleEntryMapper.toResponse(scheduleEntry);
    }

    public void deleteScheduleEntry(Long id) {
        if (id == null || !scheduleEntryRepo.existsById(id)) {
            throw new IllegalArgumentException("ScheduleEntry ID must be valid");
        }
        ScheduleEntry entry = scheduleEntryRepo.getReferenceById(id);
        entry.getWorkoutDate().removeScheduleEntry(entry);
        scheduleEntryRepo.deleteById(id);
    }

    public ScheduleEntryResponse getScheduleEntry(Long id) {
        if (id == null || !scheduleEntryRepo.existsById(id)) {
            throw new IllegalArgumentException("ScheduleEntry ID must be valid");
        }
        return ScheduleEntryMapper.toResponse(scheduleEntryRepo.getReferenceById(id));
    }

    public List<ScheduleEntryResponse> getAllScheduleEntries() {
        return ScheduleEntryMapper.toResponseList(scheduleEntryRepo.findAll());
    }
}
