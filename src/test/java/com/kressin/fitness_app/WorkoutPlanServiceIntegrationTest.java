package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.kressin.fitness_app.dto.WorkoutPlanResponse;
import com.kressin.fitness_app.entity.ScheduleType;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.repository.WorkoutDateRepository;
import com.kressin.fitness_app.repository.WorkoutPlanRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.WorkoutPlanService;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutDateCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutPlanCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutPlanCommand;

@SpringBootTest
public class WorkoutPlanServiceIntegrationTest {
    @Autowired
    WorkoutPlanService workoutPlanService;

    @Autowired
    WorkoutPlanRepository workoutPlanRepo;

    @Autowired
    WorkoutDateRepository workoutDateRepo;

    @Autowired
    WorkoutRepository workoutRepo;

    CreateWorkoutPlanCommand createCommand;
    CreateWorkoutDateCommand createWorkoutDateCommand;
    ScheduleType scheduleType;
    List<CreateScheduleEntryCommand> entries;
    Workout workout;

    @BeforeEach
    void setUp() {
        workout = new Workout(
                "Nome do Workout",
                "Descrição do Workout");
        workout = workoutRepo.saveAndFlush(workout);

        entries = new ArrayList<>();
        CreateScheduleEntryCommand firstEntry = new CreateScheduleEntryCommand(
                0,
                ZonedDateTime.now());
        entries.add(firstEntry);
        CreateScheduleEntryCommand secondEntry = new CreateScheduleEntryCommand(
                0,
                ZonedDateTime.now());
        entries.add(secondEntry);
        CreateScheduleEntryCommand thirdEntry = new CreateScheduleEntryCommand(
                0,
                ZonedDateTime.now());
        entries.add(thirdEntry);

        scheduleType = ScheduleType.RECURRING;

        createWorkoutDateCommand = new CreateWorkoutDateCommand(
                scheduleType,
                entries);

        createCommand = new CreateWorkoutPlanCommand(
                workout.getId(),
                createWorkoutDateCommand);
    }

    @AfterEach
    void cleanUp() {
        workoutPlanRepo.deleteAll();
        workoutDateRepo.deleteAll();
        workoutRepo.deleteAll();
    }

    @Test
    void shouldCreateWorkoutPlan() {
        WorkoutPlanResponse createResponse = workoutPlanService.addWorkoutPlan(createCommand);
        assertNotNull(createResponse);
        assertNotNull(createResponse.id());
        assertEquals(workout.getId(), createResponse.workout().id());
        assertEquals(scheduleType, createResponse.workoutDate().scheduleType());
        assertEquals(entries.size(), createResponse.workoutDate().scheduleEntries().size());
    }

    @Test
    void shouldNotCreateWithInvalidWorkout() {
        Long randomID = 333L;
        createCommand = new CreateWorkoutPlanCommand(
                randomID,
                createWorkoutDateCommand);
        assertThrows(IllegalArgumentException.class, () -> workoutPlanService.addWorkoutPlan(createCommand));
        assertEquals(0, workoutPlanRepo.count());
        assertEquals(0, workoutDateRepo.count());
    }

    @Test
    void shouldNotCreateWithInvalidWorkoutDate() {
        createWorkoutDateCommand = new CreateWorkoutDateCommand(
                null,
                entries);
        createCommand = new CreateWorkoutPlanCommand(
                workout.getId(),
                createWorkoutDateCommand);
        assertThrows(IllegalArgumentException.class, () -> workoutPlanService.addWorkoutPlan(createCommand));
        assertEquals(0, workoutPlanRepo.count());
        assertEquals(0, workoutDateRepo.count());
    }

    @Test
    void shouldUpdateWorkout() {
        Workout newWorkout = new Workout(
                "Nome do Workout",
                "Descrição do Workout");
        newWorkout = workoutRepo.saveAndFlush(workout);
        WorkoutPlanResponse createResponse = workoutPlanService.addWorkoutPlan(createCommand);
        UpdateWorkoutPlanCommand updateWorkoutPlanCommand = new UpdateWorkoutPlanCommand(
                createResponse.id(),
                newWorkout.getId(),
                null);
        WorkoutPlanResponse updateResponse = workoutPlanService.updateWorkoutPlan(updateWorkoutPlanCommand);
        WorkoutPlanResponse getResponse = workoutPlanService.getWorkoutPlan(updateResponse.id());
        assertEquals(newWorkout.getId(), getResponse.workout().id());
    }

    @Test
    void shouldGetAllWorkoutPlans() {
        WorkoutPlanResponse firstWorkoutPlan = workoutPlanService.addWorkoutPlan(createCommand);
        WorkoutPlanResponse secondWorkoutPlan = workoutPlanService.addWorkoutPlan(createCommand);
        WorkoutPlanResponse thirdWorkoutPlan = workoutPlanService.addWorkoutPlan(createCommand);

        List<WorkoutPlanResponse> getAllResponse = workoutPlanService.getAllWorkoutPlans();

        assertEquals(firstWorkoutPlan.id(), getAllResponse.get(0).id());
        assertEquals(secondWorkoutPlan.id(), getAllResponse.get(1).id());
        assertEquals(thirdWorkoutPlan.id(), getAllResponse.get(2).id());
    }

    @Test
    void shouldReturnEmptyListWhenDatabaseIsEmpty() {
        assertEquals(0, workoutPlanService.getAllWorkoutPlans().size());
    }

    @Test
    void shouldDeleteWorkoutPlan() {
        WorkoutPlanResponse createResponse = workoutPlanService.addWorkoutPlan(createCommand);
        workoutPlanService.deleteWorkoutPlan(createResponse.id());
        assertEquals(0, workoutPlanRepo.count());
        assertEquals(0, workoutDateRepo.count());
    }

    @Test
    void shouldNotDeleteNullID() {
        assertThrows(IllegalArgumentException.class, () -> workoutPlanService.deleteWorkoutPlan(null));
    }

    @Test
    void shouldNotDeleteInvalidID() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class, () -> workoutPlanService.deleteWorkoutPlan(randomID));
    }

}
