package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.dto.WorkoutResponse;
import com.kressin.fitness_app.service.ExerciseService;
import com.kressin.fitness_app.service.WorkoutService;
import com.kressin.fitness_app.service.command.CreateExerciseCommand;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.CreateWorkoutCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateWorkoutCommand;

import jakarta.transaction.Transactional;

@SpringBootTest
public class WorkoutServiceIntegrationTest {
    @Autowired
    WorkoutService workoutService;

    @Autowired
    ExerciseService exerciseService;

    List<CreateExerciseSetCommand> sets;
    List<CreateExercisePlanCommand> plans;
    CreateWorkoutCommand createCommand;
    CreateExerciseCommand createExerciseCommand;
    String name;
    String description;

    @BeforeEach
    void setUp() {
        createExerciseCommand = new CreateExerciseCommand(
                "Nome do Exercício",
                "",
                "",
                "");
        ExerciseResponse createExerciseResponse = exerciseService.addExercise(createExerciseCommand);

        sets = new ArrayList<>();
        CreateExerciseSetCommand firstSet = new CreateExerciseSetCommand(10, 120.0);
        sets.add(firstSet);
        CreateExerciseSetCommand secondSet = new CreateExerciseSetCommand(10, 120.0);
        sets.add(secondSet);
        CreateExerciseSetCommand thirdSet = new CreateExerciseSetCommand(10, 120.0);
        sets.add(thirdSet);

        plans = new ArrayList<>();
        CreateExercisePlanCommand createFirstExercisePlanCommand = new CreateExercisePlanCommand(
                createExerciseResponse.id(),
                sets);
        plans.add(createFirstExercisePlanCommand);
        CreateExercisePlanCommand createSecondExercisePlanCommand = new CreateExercisePlanCommand(
                createExerciseResponse.id(),
                sets);
        plans.add(createSecondExercisePlanCommand);
        CreateExercisePlanCommand createThirdExercisePlanCommand = new CreateExercisePlanCommand(
                createExerciseResponse.id(),
                sets);
        plans.add(createThirdExercisePlanCommand);

        name = "Nome do Workout";
        description = "Descrição do Workout";
        createCommand = new CreateWorkoutCommand(
                name,
                description,
                plans);
    }

    @Test
    @Transactional
    void shouldCreateWorkoutWithValidCommand() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        assertNotNull(createResponse);
        assertNotNull(createResponse.id());
        assertEquals(name, createResponse.name());
        assertEquals(description, createResponse.description());
        assertEquals(plans.size(), createResponse.exercisePlans().size());
        assertNotNull(createResponse.exercisePlans().get(0).id());
        assertNotNull(createResponse.exercisePlans().get(1).id());
        assertNotNull(createResponse.exercisePlans().get(2).id());
    }

    @Test
    @Transactional
    void shouldCreateWithNullExercisePlansList() {
        createCommand = new CreateWorkoutCommand(
                name,
                description,
                null);
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        assertNotNull(createResponse);
        assertNotNull(createResponse.id());
        assertEquals(name, createResponse.name());
        assertEquals(description, createResponse.description());
        assertEquals(0, createResponse.exercisePlans().size());
    }

    @Test
    void shouldNotCreateIfExercisePlansThrow() {
        CreateExercisePlanCommand createExercisePlanCommandWithError = new CreateExercisePlanCommand(
                null,
                sets);
        plans.add(createExercisePlanCommandWithError);
        createCommand = new CreateWorkoutCommand(
                name,
                description,
                plans);
        assertThrows(IllegalArgumentException.class, () -> workoutService.addWorkout(createCommand));
        assertEquals(0, workoutService.getAllWorkouts().size());
    }

    @Test
    @Transactional
    void shouldUpdateExercisePlanName() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        name = "Novo Nome";
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                name,
                null,
                null);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(name, updateResponse.name());
        assertNotEquals(createCommand.name(), updateResponse.name());
    }

    @Test
    @Transactional
    void shouldNotUpdateExercisePlanWithNullName() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                null,
                null,
                null);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(name, updateResponse.name());
        assertEquals(createCommand.name(), updateResponse.name());
    }

    @Test
    @Transactional
    void shouldNotUpdateExercisePlanWithBlankName() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                "",
                null,
                null);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(name, updateResponse.name());
        assertEquals(createCommand.name(), updateResponse.name());
    }

    @Test
    @Transactional
    void shouldUpdateExercisePlanDescription() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        description = "Nova Descrição";
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                null,
                description,
                null);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(description, updateResponse.description());
        assertNotEquals(createCommand.description(), updateResponse.description());
    }

    @Test
    @Transactional
    void shouldNotUpdateExercisePlanWithNullDescription() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                null,
                null,
                null);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(description, updateResponse.description());
        assertEquals(createCommand.description(), updateResponse.description());
    }

    @Test
    @Transactional
    void shouldUpdateExercisePlans() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        ExerciseResponse newExercise = exerciseService.addExercise(createExerciseCommand);
        List<UpdateExercisePlanCommand> exercisePlansToUpdate = new ArrayList<>();
        UpdateExercisePlanCommand updateExercisePlanCommand = new UpdateExercisePlanCommand(
                createResponse.exercisePlans().get(0).id(),
                null,
                newExercise.id(),
                null);
        exercisePlansToUpdate.add(updateExercisePlanCommand);
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                null,
                null,
                exercisePlansToUpdate);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.exercisePlans().get(0).id(), updateResponse.exercisePlans().get(0).id());
        assertEquals(newExercise.id(), updateResponse.exercisePlans().get(0).exercise().id());
        assertNotEquals(createResponse.exercisePlans().get(0).exercise().id(),
                updateResponse.exercisePlans().get(0).exercise().id());
    }

    @Test
    @Transactional
    void shouldAddExercisePlanWhileUpdating() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        ExerciseResponse newExercise = exerciseService.addExercise(createExerciseCommand);
        List<UpdateExercisePlanCommand> exercisePlansToUpdate = new ArrayList<>();
        UpdateExercisePlanCommand updateExercisePlanCommand = new UpdateExercisePlanCommand(
                null,
                null,
                newExercise.id(),
                null);
        exercisePlansToUpdate.add(updateExercisePlanCommand);
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                null,
                null,
                exercisePlansToUpdate);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(4, updateResponse.exercisePlans().size());
        assertEquals(newExercise.id(), updateResponse.exercisePlans().get(3).exercise().id());
    }

    @Test
    @Transactional
    void shouldDeleteExercisePlanWhileUpdating() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        List<UpdateExercisePlanCommand> exercisePlansToUpdate = new ArrayList<>();
        UpdateExercisePlanCommand updateExercisePlanCommand = new UpdateExercisePlanCommand(
                createResponse.exercisePlans().get(2).id(),
                true,
                null,
                null);
        exercisePlansToUpdate.add(updateExercisePlanCommand);
        UpdateWorkoutCommand updateCommand = new UpdateWorkoutCommand(
                createResponse.id(),
                null,
                null,
                exercisePlansToUpdate);
        WorkoutResponse updateResponse = workoutService.updateWorkout(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(2, updateResponse.exercisePlans().size());
        assertNotEquals(createResponse.exercisePlans().get(2).id(), updateResponse.exercisePlans().get(0).id());
        assertNotEquals(createResponse.exercisePlans().get(2).id(), updateResponse.exercisePlans().get(1).id());
    }

    @Test
    @Transactional
    void shouldGetWorkout() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        WorkoutResponse getResponse = workoutService.getWorkout(createResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(createResponse.id(), getResponse.id());
    }

    @Test
    @Transactional
    void shouldNotGetWorkoutWithNullID() {
        assertThrows(IllegalArgumentException.class, () -> workoutService.getWorkout(null));
    }

    @Test
    @Transactional
    void shouldNotGetWorkoutWithInvalidID() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class, () -> workoutService.getWorkout(randomID));
    }

    @Test
    @Transactional
    void shouldGetAllWorkouts() {
        WorkoutResponse firstWorkout = workoutService.addWorkout(createCommand);
        WorkoutResponse secondWorkout = workoutService.addWorkout(createCommand);
        WorkoutResponse thirdWorkout = workoutService.addWorkout(createCommand);

        List<WorkoutResponse> getAllResponse = workoutService.getAllWorkouts();
        assertEquals(3, getAllResponse.size());
        assertEquals(firstWorkout.id(), getAllResponse.get(0).id());
        assertEquals(secondWorkout.id(), getAllResponse.get(1).id());
        assertEquals(thirdWorkout.id(), getAllResponse.get(2).id());
    }

    @Test
    @Transactional
    void shouldGetEmptyListWhenDatabaseIsEmpty() {
        List<WorkoutResponse> allWorkouts = workoutService.getAllWorkouts();
        assertEquals(0, allWorkouts.size());
    }

    @Test
    @Transactional
    void shouldDeleteWorkout() {
        WorkoutResponse createResponse = workoutService.addWorkout(createCommand);
        workoutService.deleteWorkout(createResponse.id());
        assertThrows(IllegalArgumentException.class, () -> workoutService.getWorkout(createResponse.id()));
        assertEquals(0, workoutService.getAllWorkouts().size());
    }

    @Test
    @Transactional
    void shouldNotDeleteNullID() {
        assertThrows(IllegalArgumentException.class, () -> workoutService.deleteWorkout(null));
    }

    @Test
    @Transactional
    void shouldNotDeleteInvalidWorkout() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class, () -> workoutService.deleteWorkout(randomID));
    }
}
