package com.kressin.fitness_app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kressin.fitness_app.dto.ScheduleEntryResponse;
import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.exception.ScheduleEntryNotFoundException;
import com.kressin.fitness_app.mapper.ScheduleEntryMapper;
import com.kressin.fitness_app.repository.ScheduleEntryRepository;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;

import jakarta.transaction.Transactional;

@Service
public class ScheduleEntryService {
    private final ScheduleEntryRepository scheduleEntryRepo;

    public ScheduleEntryService(ScheduleEntryRepository scheduleEntryRepo) {
        this.scheduleEntryRepo = scheduleEntryRepo;
    }

    @Transactional
    public ScheduleEntryResponse addScheduleEntry(CreateScheduleEntryCommand command, WorkoutDate workoutDate) {
        if (command.weekDay() == null || command.dateTime() == null) {
            throw new BusinessException("Create Command must have valid weekDay and dateTime");
        }
        ScheduleEntry scheduleEntry = new ScheduleEntry(
                command.weekDay(),
                command.dateTime(),
                workoutDate);
        scheduleEntry = scheduleEntryRepo.save(scheduleEntry);
        workoutDate.addScheduleEntry(scheduleEntry);
        return ScheduleEntryMapper.toResponse(scheduleEntry);
    }

    @Transactional
    public ScheduleEntryResponse updateScheduleEntry(UpdateScheduleEntryCommand command) {
        if (command.shouldDelete() != null && command.shouldDelete()) {
            deleteScheduleEntry(command.id());
            return null;
        }

        ScheduleEntry scheduleEntry = scheduleEntryRepo.findById(command.id())
                .orElseThrow(() -> new ScheduleEntryNotFoundException(command.id()));

        if (command.weekDay() != null) {
            scheduleEntry.setWeekDay(command.weekDay());
        }

        if (command.dateTime() != null) {
            scheduleEntry.setDateTime(command.dateTime());
        }

        return ScheduleEntryMapper.toResponse(scheduleEntry);
    }

    @Transactional
    public void deleteScheduleEntry(Long id) {
        ScheduleEntry entry = scheduleEntryRepo.findById(id).orElseThrow(() -> new ScheduleEntryNotFoundException(id));
        entry.getWorkoutDate().removeScheduleEntry(entry);
        scheduleEntryRepo.deleteById(id);
    }

    public ScheduleEntryResponse getScheduleEntry(Long id) {
        return ScheduleEntryMapper
                .toResponse(scheduleEntryRepo.findById(id).orElseThrow(() -> new ScheduleEntryNotFoundException(id)));
    }

    public List<ScheduleEntryResponse> getAllScheduleEntries() {
        return ScheduleEntryMapper.toResponseList(scheduleEntryRepo.findAll());
    }
}
