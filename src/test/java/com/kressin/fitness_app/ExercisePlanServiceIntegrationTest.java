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

import com.kressin.fitness_app.dto.ExercisePlanResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.ExercisePlanService;
import com.kressin.fitness_app.service.command.CreateExercisePlanCommand;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateExercisePlanCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ExercisePlanServiceIntegrationTest {
    @Autowired
    ExercisePlanService exercisePlanService;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    WorkoutRepository workoutRepository;

    CreateExercisePlanCommand createCommand;
    List<CreateExerciseSetCommand> sets;
    Workout workout;
    Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = exerciseRepository.save(new Exercise("Nome do Exercício",
                "",
                "",
                ""));
        workout = workoutRepository.save(new Workout(
                "Nome do Workout",
                ""));

        sets = new ArrayList<>();
        CreateExerciseSetCommand firstSet = new CreateExerciseSetCommand(10, 120.0);
        sets.add(firstSet);
        CreateExerciseSetCommand secondSet = new CreateExerciseSetCommand(10, 120.0);
        sets.add(secondSet);
        CreateExerciseSetCommand thirdSet = new CreateExerciseSetCommand(10, 120.0);
        sets.add(thirdSet);

        createCommand = new CreateExercisePlanCommand(
                exercise.getId(),
                sets);
    }

    @Test
    void shouldCreateExercisePlanWithValidCommand() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        assertNotNull(createResponse);
        assertNotNull(createResponse.id());
        assertEquals(exercise.getId(), createResponse.exercise().id());
        assertEquals(3, createResponse.exerciseSets().size());
        assertEquals(10, createResponse.exerciseSets().get(0).reps());
        assertEquals(10, createResponse.exerciseSets().get(1).reps());
        assertEquals(10, createResponse.exerciseSets().get(2).reps());
        assertEquals(120.0, createResponse.exerciseSets().get(0).weight());
        assertEquals(120.0, createResponse.exerciseSets().get(1).weight());
        assertEquals(120.0, createResponse.exerciseSets().get(2).weight());
    }

    @Test
    void shouldCreateExercisePlanWithNullSets() {
        createCommand = new CreateExercisePlanCommand(
                exercise.getId(),
                null);
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        assertNotNull(createResponse);
        assertNotNull(createResponse.id());
        assertEquals(exercise.getId(), createResponse.exercise().id());
        assertEquals(0, createResponse.exerciseSets().size());
    }

    @Test
    void shouldNotCreateExercisePlanWithNullWorkout() {
        workout = null;
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.addExercisePlan(createCommand, workout));
    }

    @Test
    void shouldNotCreateExercisePlanWithNullExerciseID() {
        createCommand = new CreateExercisePlanCommand(
                null,
                sets);
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.addExercisePlan(createCommand, workout));
    }

    @Test
    void shouldNotCreateExercisePlanWithInvalidExerciseID() {
        createCommand = new CreateExercisePlanCommand(
                333L,
                sets);
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.addExercisePlan(createCommand, workout));
    }

    @Test
    void shouldUpdateExercisePlanSetsAndExercise() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        Exercise newExercise = exerciseRepository.save(new Exercise("Nome do Exercício",
                "",
                "",
                ""));

        Integer newReps = 20;
        Double newWeight = 240.0;

        List<UpdateExerciseSetCommand> updatedSets = new ArrayList<>();
        UpdateExerciseSetCommand firstSet = new UpdateExerciseSetCommand(createResponse.exerciseSets().get(0).id(),
                null, newReps, newWeight);
        updatedSets.add(firstSet);
        UpdateExerciseSetCommand secondSet = new UpdateExerciseSetCommand(createResponse.exerciseSets().get(1).id(),
                null, newReps, newWeight);
        updatedSets.add(secondSet);
        UpdateExerciseSetCommand thirdSet = new UpdateExerciseSetCommand(createResponse.exerciseSets().get(2).id(),
                null, newReps, newWeight);
        updatedSets.add(thirdSet);

        UpdateExercisePlanCommand updateCommand = new UpdateExercisePlanCommand(
                createResponse.id(),
                null,
                newExercise.getId(),
                updatedSets);

        ExercisePlanResponse updateResponse = exercisePlanService.updateExercisePlan(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newExercise.getId(), updateResponse.exercise().id());
        assertEquals(3, updateResponse.exerciseSets().size());
        assertEquals(createResponse.exerciseSets().get(0).id(), updateResponse.exerciseSets().get(0).id());
        assertEquals(createResponse.exerciseSets().get(1).id(), updateResponse.exerciseSets().get(1).id());
        assertEquals(createResponse.exerciseSets().get(2).id(), updateResponse.exerciseSets().get(2).id());
        assertEquals(newReps, updateResponse.exerciseSets().get(0).reps());
        assertEquals(newReps, updateResponse.exerciseSets().get(1).reps());
        assertEquals(newReps, updateResponse.exerciseSets().get(2).reps());
        assertEquals(newWeight, updateResponse.exerciseSets().get(0).weight());
        assertEquals(newWeight, updateResponse.exerciseSets().get(1).weight());
        assertEquals(newWeight, updateResponse.exerciseSets().get(2).weight());
    }

    @Test
    void shouldUpdateExercisePlanExercise() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        Exercise newExercise = exerciseRepository.save(new Exercise("Nome do Exercício",
                "",
                "",
                ""));

        UpdateExercisePlanCommand updateCommand = new UpdateExercisePlanCommand(
                createResponse.id(),
                null,
                newExercise.getId(),
                null);

        ExercisePlanResponse updateResponse = exercisePlanService.updateExercisePlan(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newExercise.getId(), updateResponse.exercise().id());
        assertEquals(3, updateResponse.exerciseSets().size());
        assertEquals(createResponse.exerciseSets().get(0).id(), updateResponse.exerciseSets().get(0).id());
        assertEquals(createResponse.exerciseSets().get(1).id(), updateResponse.exerciseSets().get(1).id());
        assertEquals(createResponse.exerciseSets().get(2).id(), updateResponse.exerciseSets().get(2).id());
        assertEquals(createResponse.exerciseSets().get(0).reps(), updateResponse.exerciseSets().get(0).reps());
        assertEquals(createResponse.exerciseSets().get(1).reps(), updateResponse.exerciseSets().get(1).reps());
        assertEquals(createResponse.exerciseSets().get(2).reps(), updateResponse.exerciseSets().get(2).reps());
        assertEquals(createResponse.exerciseSets().get(0).weight(), updateResponse.exerciseSets().get(0).weight());
        assertEquals(createResponse.exerciseSets().get(1).weight(), updateResponse.exerciseSets().get(1).weight());
        assertEquals(createResponse.exerciseSets().get(2).weight(), updateResponse.exerciseSets().get(2).weight());
    }

    @Test
    void shouldUpdateExercisePlanSets() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);

        Integer newReps = 20;
        Double newWeight = 240.0;

        List<UpdateExerciseSetCommand> updatedSets = new ArrayList<>();
        UpdateExerciseSetCommand firstSet = new UpdateExerciseSetCommand(createResponse.exerciseSets().get(0).id(),
                null, newReps, newWeight);
        updatedSets.add(firstSet);
        UpdateExerciseSetCommand secondSet = new UpdateExerciseSetCommand(createResponse.exerciseSets().get(1).id(),
                null, newReps, newWeight);
        updatedSets.add(secondSet);
        UpdateExerciseSetCommand thirdSet = new UpdateExerciseSetCommand(createResponse.exerciseSets().get(2).id(),
                null, newReps, newWeight);
        updatedSets.add(thirdSet);

        UpdateExercisePlanCommand updateCommand = new UpdateExercisePlanCommand(
                createResponse.id(),
                null,
                null,
                updatedSets);

        ExercisePlanResponse updateResponse = exercisePlanService.updateExercisePlan(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.exercise().id(), updateResponse.exercise().id());
        assertEquals(3, updateResponse.exerciseSets().size());
        assertEquals(createResponse.exerciseSets().get(0).id(), updateResponse.exerciseSets().get(0).id());
        assertEquals(createResponse.exerciseSets().get(1).id(), updateResponse.exerciseSets().get(1).id());
        assertEquals(createResponse.exerciseSets().get(2).id(), updateResponse.exerciseSets().get(2).id());
        assertEquals(newReps, updateResponse.exerciseSets().get(0).reps());
        assertEquals(newReps, updateResponse.exerciseSets().get(1).reps());
        assertEquals(newReps, updateResponse.exerciseSets().get(2).reps());
        assertEquals(newWeight, updateResponse.exerciseSets().get(0).weight());
        assertEquals(newWeight, updateResponse.exerciseSets().get(1).weight());
        assertEquals(newWeight, updateResponse.exerciseSets().get(2).weight());
    }

    @Test
    void shouldUpdateExercisePlanWithNewSet() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);

        Integer newReps = 20;
        Double newWeight = 240.0;

        List<UpdateExerciseSetCommand> updatedSets = new ArrayList<>();
        UpdateExerciseSetCommand newSet = new UpdateExerciseSetCommand(
                null,
                null,
                newReps,
                newWeight);
        updatedSets.add(newSet);

        UpdateExercisePlanCommand updateCommand = new UpdateExercisePlanCommand(
                createResponse.id(),
                null,
                null,
                updatedSets);

        ExercisePlanResponse updateResponse = exercisePlanService.updateExercisePlan(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.exercise().id(), updateResponse.exercise().id());
        assertEquals(4, updateResponse.exerciseSets().size());
        assertEquals(newReps, updateResponse.exerciseSets().get(3).reps());
        assertEquals(newWeight, updateResponse.exerciseSets().get(3).weight());
    }

    @Test
    void shouldUpdateExercisePlanWithDeletedSet() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        List<UpdateExerciseSetCommand> updatedSets = new ArrayList<>();
        UpdateExerciseSetCommand deleteSet = new UpdateExerciseSetCommand(
                createResponse.exerciseSets().get(1).id(),
                true,
                null,
                null);
        updatedSets.add(deleteSet);

        UpdateExercisePlanCommand updateCommand = new UpdateExercisePlanCommand(
                createResponse.id(),
                null,
                null,
                updatedSets);

        ExercisePlanResponse updateResponse = exercisePlanService.updateExercisePlan(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.exercise().id(), updateResponse.exercise().id());
        assertEquals(2, updateResponse.exerciseSets().size());
        assertNotEquals(createResponse.exerciseSets().get(1).id(), updateResponse.exerciseSets().get(0).id());
        assertNotEquals(createResponse.exerciseSets().get(1).id(), updateResponse.exerciseSets().get(1).id());
    }

    @Test
    void shouldDeleteExercisePlan() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        exercisePlanService.deleteExercisePlan(createResponse.id());
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.getExercisePlan(createResponse.id()));
        assertEquals(0, exercisePlanService.getAllExercisePlans().size());
    }

    @Test
    void shouldThrowDeletingExercisePlanWithNullID() {
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.deleteExercisePlan(null));
    }

    @Test
    void shouldThrowDeletingInvalidExercisePlan() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.deleteExercisePlan(randomID));
    }

    @Test
    void shouldGetExercisePlan() {
        ExercisePlanResponse createResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        ExercisePlanResponse getResponse = exercisePlanService.getExercisePlan(createResponse.id());
        assertEquals(createResponse.id(), getResponse.id());
        assertEquals(createResponse.exercise().id(), getResponse.exercise().id());
        assertEquals(createResponse.exerciseSets().size(), getResponse.exerciseSets().size());
        assertEquals(createResponse.exerciseSets().get(0).id(), getResponse.exerciseSets().get(0).id());
        assertEquals(createResponse.exerciseSets().get(1).id(), getResponse.exerciseSets().get(1).id());
        assertEquals(createResponse.exerciseSets().get(2).id(), getResponse.exerciseSets().get(2).id());
    }

    @Test
    void shouldThrowWhenGetNullPlanID() {
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.getExercisePlan(null));
    }

    @Test
    void shouldThrowWhenGetInvalidPlanID() {
        Long randomID = 333L;
        assertThrows(IllegalArgumentException.class, () -> exercisePlanService.getExercisePlan(randomID));
    }

    @Test
    void shouldReturnWhenGetAllExercisePlans() {
        ExercisePlanResponse firstCreateResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        ExercisePlanResponse secondCreateResponse = exercisePlanService.addExercisePlan(createCommand, workout);
        ExercisePlanResponse thirdCreateResponse = exercisePlanService.addExercisePlan(createCommand, workout);

        List<ExercisePlanResponse> getAllResponse = exercisePlanService.getAllExercisePlans();
        assertEquals(3, getAllResponse.size());
        assertEquals(firstCreateResponse.id(), getAllResponse.get(0).id());
        assertEquals(secondCreateResponse.id(), getAllResponse.get(1).id());
        assertEquals(thirdCreateResponse.id(), getAllResponse.get(2).id());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllExercisePlansWithEmptyDatabase() {
        List<ExercisePlanResponse> getAllResponse = exercisePlanService.getAllExercisePlans();
        assertEquals(0, getAllResponse.size());
    }

}
