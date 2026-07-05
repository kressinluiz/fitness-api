package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kressin.fitness_app.dto.ScheduleEntryResponse;
import com.kressin.fitness_app.entity.ScheduleType;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.repository.WorkoutDateRepository;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.ScheduleEntryService;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ScheduleEntryServiceIntegrationTest {
    @Autowired
    ScheduleEntryService scheduleEntryService;

    @Autowired
    WorkoutRepository workoutRepo;

    @Autowired
    WorkoutPlanRepository workoutPlanRepo;

    @Autowired
    WorkoutDateRepository workoutDateRepo;

    CreateScheduleEntryCommand createCommand;
    WorkoutDate workoutDate;
    Integer weekDay;
    ZonedDateTime dateTime;

    @BeforeEach
    void setUp() {
        Workout workout = new Workout("Nome do Workout", "Descrição do Workout");
        WorkoutPlan workoutPlan = new WorkoutPlan(workout);
        workoutDate = new WorkoutDate(workoutPlan, ScheduleType.RECURRING);
        workoutRepo.save(workout);
        workoutPlanRepo.save(workoutPlan);
        workoutDateRepo.save(workoutDate);
        weekDay = 0;
        dateTime = ZonedDateTime.now();
        createCommand = new CreateScheduleEntryCommand(weekDay, dateTime);
    }

    @Test
    void shouldCreateScheduleEntryWithValidCommand() {
        ScheduleEntryResponse addResponse = scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
        assertNotNull(addResponse);
        assertNotNull(addResponse.id());
        assertEquals(weekDay, addResponse.weekDay());
        assertEquals(dateTime, addResponse.dateTime());
    }

    @Test
    void shouldUpdateAllFields() {
        Integer newWeekDay = 1;
        ZonedDateTime newDateTime = ZonedDateTime.now();
        ScheduleEntryResponse addResponse = scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
        UpdateScheduleEntryCommand updateCommand = new UpdateScheduleEntryCommand(
                addResponse.id(),
                newWeekDay,
                newDateTime);
        ScheduleEntryResponse updateResponse = scheduleEntryService.updateScheduleEntry(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newWeekDay, updateResponse.weekDay());
        assertEquals(newDateTime, updateResponse.dateTime());

        ScheduleEntryResponse getResponse = scheduleEntryService.getScheduleEntry(updateResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(newWeekDay, getResponse.weekDay());
        assertEquals(newDateTime, getResponse.dateTime());
    }

    @Test
    void shouldUpdateWeekDay() {
        Integer newWeekDay = 1;
        ScheduleEntryResponse addResponse = scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
        UpdateScheduleEntryCommand updateCommand = new UpdateScheduleEntryCommand(
                addResponse.id(),
                newWeekDay,
                null);
        ScheduleEntryResponse updateResponse = scheduleEntryService.updateScheduleEntry(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newWeekDay, updateResponse.weekDay());
        assertEquals(addResponse.dateTime(), updateResponse.dateTime());

        ScheduleEntryResponse getResponse = scheduleEntryService.getScheduleEntry(updateResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(newWeekDay, getResponse.weekDay());
        assertEquals(addResponse.dateTime(), getResponse.dateTime());
    }

    @Test
    void shouldUpdateDateTime() {
        ZonedDateTime newDateTime = ZonedDateTime.now();
        ScheduleEntryResponse addResponse = scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
        UpdateScheduleEntryCommand updateCommand = new UpdateScheduleEntryCommand(
                addResponse.id(),
                null,
                newDateTime);
        ScheduleEntryResponse updateResponse = scheduleEntryService.updateScheduleEntry(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(addResponse.weekDay(), updateResponse.weekDay());
        assertEquals(newDateTime, updateResponse.dateTime());

        ScheduleEntryResponse getResponse = scheduleEntryService.getScheduleEntry(updateResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(addResponse.weekDay(), getResponse.weekDay());
        assertEquals(newDateTime, getResponse.dateTime());
    }

    @Test
    void shouldNotUpdateWithNullID() {
        Integer newWeekDay = 1;
        ZonedDateTime newDateTime = ZonedDateTime.now();
        UpdateScheduleEntryCommand updateCommand = new UpdateScheduleEntryCommand(
                null,
                newWeekDay,
                newDateTime);
        assertThrows(IllegalArgumentException.class, () -> scheduleEntryService.updateScheduleEntry(updateCommand));
    }

    @Test
    void shouldNotUpdateWithInvalidID() {
        Integer newWeekDay = 1;
        Long randomID = 333L;
        ZonedDateTime newDateTime = ZonedDateTime.now();
        UpdateScheduleEntryCommand updateCommand = new UpdateScheduleEntryCommand(
                randomID,
                newWeekDay,
                newDateTime);
        assertThrows(IllegalArgumentException.class, () -> scheduleEntryService.updateScheduleEntry(updateCommand));
    }

    @Test
    void shouldDeleteScheduleEntry() {
        ScheduleEntryResponse addResponse = scheduleEntryService.addScheduleEntry(createCommand, workoutDate);
        scheduleEntryService.deleteScheduleEntry(addResponse.id());
        assertEquals(0, scheduleEntryService.getAllScheduleEntries().size());
    }

    @Test
    void shouldNotDeleteNullID() {
        assertThrows(IllegalArgumentException.class, () -> scheduleEntryService.deleteScheduleEntry(null));
    }

    @Test
    void shouldNotDeleteInvalidID() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class,
                () -> scheduleEntryService.deleteScheduleEntry(randomID));
    }

    @Test
    void shouldNotGetNullID() {
        assertThrows(IllegalArgumentException.class, () -> scheduleEntryService.getScheduleEntry(null));
    }

    @Test
    void shouldNotGetInvalidID() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class, () -> scheduleEntryService.getScheduleEntry(randomID));
    }

    @Test
    void shouldReturnEmptyListWhenDatabaseIsEmpty() {
        assertEquals(0, scheduleEntryService.getAllScheduleEntries().size());
    }
}
