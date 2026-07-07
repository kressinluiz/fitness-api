package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kressin.fitness_app.dto.WorkoutDateResponse;
import com.kressin.fitness_app.entity.ScheduleType;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.WorkoutDateService;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutDateCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutDateCommand;

import jakarta.transaction.Transactional;

@SpringBootTest
public class WorkoutDateServiceIntegrationTest {
    @Autowired
    WorkoutDateService workoutDateService;

    @Autowired
    WorkoutPlanRepository workoutPlanRepo;

    @Autowired
    WorkoutRepository workoutRepo;

    CreateWorkoutDateCommand createCommand;
    WorkoutPlan workoutPlan;
    ScheduleType scheduleType;
    List<CreateScheduleEntryCommand> entries;
    Workout workout;

    @BeforeEach
    void setUp() {
        workout = new Workout("Nome do Workout", "Descrição do Workout");
        workoutRepo.save(workout);
        workoutPlan = new WorkoutPlan(workout);
        workoutPlanRepo.save(workoutPlan);

        entries = new ArrayList<>();
        CreateScheduleEntryCommand firstEntry = new CreateScheduleEntryCommand(0, ZonedDateTime.now());
        entries.add(firstEntry);
        CreateScheduleEntryCommand secondEntry = new CreateScheduleEntryCommand(0, ZonedDateTime.now());
        entries.add(secondEntry);
        CreateScheduleEntryCommand thirdEntry = new CreateScheduleEntryCommand(0, ZonedDateTime.now());
        entries.add(thirdEntry);

        scheduleType = ScheduleType.RECURRING;

        createCommand = new CreateWorkoutDateCommand(
                scheduleType,
                entries);
    }

    @AfterEach
    void cleanUp() {
        workoutRepo.deleteAll();
        workoutPlanRepo.deleteAll();
    }

    @Test
    @Transactional
    void shouldCreateWorkoutDateWithValidCommand() {
        WorkoutDateResponse addResponse = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        assertNotNull(addResponse);
        assertNotNull(addResponse.id());
        assertEquals(scheduleType, addResponse.scheduleType());
        assertEquals(3, addResponse.scheduleEntries().size());
        assertEquals(0, addResponse.scheduleEntries().get(0).weekDay());
        assertEquals(0, addResponse.scheduleEntries().get(1).weekDay());
        assertEquals(0, addResponse.scheduleEntries().get(2).weekDay());
        WorkoutDateResponse getResponse = workoutDateService.getWorkoutDate(addResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(scheduleType, getResponse.scheduleType());
        assertEquals(3, getResponse.scheduleEntries().size());
        assertEquals(0, getResponse.scheduleEntries().get(0).weekDay());
        assertEquals(0, getResponse.scheduleEntries().get(1).weekDay());
        assertEquals(0, getResponse.scheduleEntries().get(2).weekDay());
    }

    @Test
    @Transactional
    void shouldCreateWorkoutDateWithNullEntries() {
        createCommand = new CreateWorkoutDateCommand(
                scheduleType,
                null);
        WorkoutDateResponse addResponse = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        assertNotNull(addResponse);
        assertNotNull(addResponse.id());
        assertEquals(scheduleType, addResponse.scheduleType());
        assertEquals(0, addResponse.scheduleEntries().size());
        WorkoutDateResponse getResponse = workoutDateService.getWorkoutDate(addResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(scheduleType, getResponse.scheduleType());
        assertEquals(0, getResponse.scheduleEntries().size());
    }

    @Test
    void shouldNotCreateWorkoutDateIfEntriesThrow() {
        CreateScheduleEntryCommand errorEntry = new CreateScheduleEntryCommand(null, ZonedDateTime.now());
        entries.add(errorEntry);
        createCommand = new CreateWorkoutDateCommand(
                scheduleType,
                entries);
        assertThrows(BusinessException.class,
                () -> workoutDateService.addWorkoutDate(createCommand, workoutPlan));
        assertEquals(0, workoutDateService.getAllWorkoutDates().size());
    }

    @Test
    void shouldUpdateScheduleType() {
        ScheduleType newScheduleType = ScheduleType.SPECIFIC_DATES;
        WorkoutDateResponse addResponse = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        UpdateWorkoutDateCommand updateCommand = new UpdateWorkoutDateCommand(
                addResponse.id(),
                newScheduleType,
                null);
        WorkoutDateResponse updateResponse = workoutDateService.updateWorkoutDate(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newScheduleType, updateResponse.scheduleType());
        assertEquals(3, updateResponse.scheduleEntries().size());
        assertEquals(0, updateResponse.scheduleEntries().get(0).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(1).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(2).weekDay());
    }

    @Test
    void shouldUpdateEntry() {
        Integer newWeekDay = 2;
        WorkoutDateResponse addResponse = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        List<UpdateScheduleEntryCommand> entryUpdateCommandList = new ArrayList<>();
        UpdateScheduleEntryCommand updateEntryCommand = new UpdateScheduleEntryCommand(
                addResponse.scheduleEntries().get(0).id(),
                null,
                newWeekDay,
                null);
        entryUpdateCommandList.add(updateEntryCommand);
        UpdateWorkoutDateCommand updateCommand = new UpdateWorkoutDateCommand(
                addResponse.id(),
                null,
                entryUpdateCommandList);
        WorkoutDateResponse updateResponse = workoutDateService.updateWorkoutDate(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(scheduleType, updateResponse.scheduleType());
        assertEquals(3, updateResponse.scheduleEntries().size());
        assertEquals(newWeekDay, updateResponse.scheduleEntries().get(0).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(1).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(2).weekDay());
    }

    @Test
    void shouldAddEntryWhenUpdate() {
        WorkoutDateResponse addResponse = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        List<UpdateScheduleEntryCommand> entryUpdateCommandList = new ArrayList<>();
        UpdateScheduleEntryCommand newEntry = new UpdateScheduleEntryCommand(
                null,
                null,
                0,
                ZonedDateTime.now());
        entryUpdateCommandList.add(newEntry);
        UpdateWorkoutDateCommand updateCommand = new UpdateWorkoutDateCommand(
                addResponse.id(),
                null,
                entryUpdateCommandList);
        WorkoutDateResponse updateResponse = workoutDateService.updateWorkoutDate(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(scheduleType, updateResponse.scheduleType());
        assertEquals(4, updateResponse.scheduleEntries().size());
        assertEquals(0, updateResponse.scheduleEntries().get(0).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(1).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(2).weekDay());
        assertEquals(0, updateResponse.scheduleEntries().get(3).weekDay());
    }

    @Test
    void shouldDeleteEntryWhenUpdate() {
        WorkoutDateResponse addResponse = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        List<UpdateScheduleEntryCommand> entryUpdateCommandList = new ArrayList<>();
        UpdateScheduleEntryCommand deleteEntry = new UpdateScheduleEntryCommand(
                addResponse.scheduleEntries().get(1).id(),
                true,
                0,
                ZonedDateTime.now());
        entryUpdateCommandList.add(deleteEntry);
        UpdateWorkoutDateCommand updateCommand = new UpdateWorkoutDateCommand(
                addResponse.id(),
                null,
                entryUpdateCommandList);
        WorkoutDateResponse updateResponse = workoutDateService.updateWorkoutDate(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(scheduleType, updateResponse.scheduleType());
        assertEquals(2, updateResponse.scheduleEntries().size());
        assertNotEquals(addResponse.scheduleEntries().get(1).id(), updateResponse.scheduleEntries().get(0).id());
        assertNotEquals(addResponse.scheduleEntries().get(1).id(), updateResponse.scheduleEntries().get(1).id());
    }

    @Test
    void shouldGetAllWorkoutDates() {
        WorkoutPlan secondWorkoutPlan = new WorkoutPlan(workout);
        WorkoutPlan thirdWorkoutPlan = new WorkoutPlan(workout);
        workoutPlanRepo.save(secondWorkoutPlan);
        workoutPlanRepo.save(thirdWorkoutPlan);
        WorkoutDateResponse firstDate = workoutDateService.addWorkoutDate(createCommand, workoutPlan);
        WorkoutDateResponse secondDate = workoutDateService.addWorkoutDate(createCommand, secondWorkoutPlan);
        WorkoutDateResponse thirdDate = workoutDateService.addWorkoutDate(createCommand, thirdWorkoutPlan);

        List<WorkoutDateResponse> workoutDateList = workoutDateService.getAllWorkoutDates();

        assertEquals(firstDate.id(), workoutDateList.get(0).id());
        assertEquals(secondDate.id(), workoutDateList.get(1).id());
        assertEquals(thirdDate.id(), workoutDateList.get(2).id());
    }

    @Test
    void shouldReturnEmptyListWhenDatabaseIsEmpty() {
        assertEquals(0, workoutDateService.getAllWorkoutDates().size());
    }

}
