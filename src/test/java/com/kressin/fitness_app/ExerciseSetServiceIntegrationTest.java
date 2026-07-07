package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kressin.fitness_app.dto.ExerciseSetResponse;
import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.exception.ExerciseSetNotFoundException;
import com.kressin.fitness_app.repository.ExercisePlanRepository;
import com.kressin.fitness_app.repository.ExerciseRepository;
import com.kressin.fitness_app.repository.WorkoutRepository;
import com.kressin.fitness_app.service.ExerciseSetService;
import com.kressin.fitness_app.service.command.CreateExerciseSetCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseSetCommand;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ExerciseSetServiceIntegrationTest {
    @Autowired
    ExerciseSetService exerciseSetService;

    @Autowired
    ExercisePlanRepository exercisePlanRepository;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    WorkoutRepository workoutRepository;

    ExercisePlan exercisePlan;
    CreateExerciseSetCommand createCommand;
    Integer reps;
    Double weight;

    @BeforeEach
    void setUp() {
        Exercise exercise = new Exercise("Nome do Exercício",
                "",
                "",
                "");
        exerciseRepository.save(exercise);
        Workout workout = new Workout(
                "Nome do Workout",
                "");
        workoutRepository.save(workout);
        exercisePlan = new ExercisePlan(exercise, workout);
        exercisePlanRepository.save(exercisePlan);

        reps = 10;
        weight = 120.0;
        createCommand = new CreateExerciseSetCommand(reps, weight);
    }

    @Test
    void shouldAddExerciseSetWithValidCommand() {
        ExerciseSetResponse addResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);
        assertNotNull(addResponse);
        assertNotNull(addResponse.id());
        assertEquals(createCommand.reps(), addResponse.reps());
        assertEquals(createCommand.weight(), addResponse.weight());
    }

    @Test
    void shouldNotAddExerciseSetWithNullReps() {
        reps = null;
        createCommand = new CreateExerciseSetCommand(reps, weight);
        assertThrows(BusinessException.class,
                () -> exerciseSetService.addExerciseSet(createCommand, exercisePlan));
        assertEquals(0, exerciseSetService.getAllExerciseSets().size());
    }

    @Test
    void shouldNotAddExerciseSetWithInvalidReps() {
        reps = -1;
        createCommand = new CreateExerciseSetCommand(reps, weight);
        assertThrows(BusinessException.class,
                () -> exerciseSetService.addExerciseSet(createCommand, exercisePlan));
        assertEquals(0, exerciseSetService.getAllExerciseSets().size());
    }

    @Test
    void shouldNotAddExerciseSetWithNullWeight() {
        weight = null;
        createCommand = new CreateExerciseSetCommand(reps, weight);
        assertThrows(BusinessException.class,
                () -> exerciseSetService.addExerciseSet(createCommand, exercisePlan));
        assertEquals(0, exerciseSetService.getAllExerciseSets().size());
    }

    @Test
    void shouldNotAddExerciseSetWithInvalidWeight() {
        weight = -1.0;
        createCommand = new CreateExerciseSetCommand(reps, weight);
        assertThrows(BusinessException.class,
                () -> exerciseSetService.addExerciseSet(createCommand, exercisePlan));
        assertEquals(0, exerciseSetService.getAllExerciseSets().size());
    }

    @Test
    void shouldNotAddExerciseSetWithNullPlan() {
        exercisePlan = null;
        createCommand = new CreateExerciseSetCommand(reps, weight);
        assertThrows(BusinessException.class,
                () -> exerciseSetService.addExerciseSet(createCommand, exercisePlan));
        assertEquals(0, exerciseSetService.getAllExerciseSets().size());
    }

    @Test
    void shouldUpdateExerciseSetWithAllFields() {
        ExerciseSetResponse addResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);

        UpdateExerciseSetCommand updateCommand = new UpdateExerciseSetCommand(
                addResponse.id(),
                false,
                20,
                240.0);

        ExerciseSetResponse updateResponse = exerciseSetService.updateExerciseSet(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(updateCommand.reps(), updateResponse.reps());
        assertEquals(updateCommand.weight(), updateResponse.weight());

        ExerciseSetResponse getResponse = exerciseSetService.getExerciseSet(addResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(updateResponse.id(), getResponse.id());
        assertEquals(updateCommand.reps(), getResponse.reps());
        assertEquals(updateCommand.weight(), getResponse.weight());
    }

    @Test
    void shouldUpdateOnlyExerciseSetReps() {
        ExerciseSetResponse addResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);
        Integer newReps = 20;
        UpdateExerciseSetCommand updateCommand = new UpdateExerciseSetCommand(
                addResponse.id(),
                false,
                newReps,
                null);

        ExerciseSetResponse updateResponse = exerciseSetService.updateExerciseSet(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newReps, updateResponse.reps());
        assertEquals(addResponse.weight(), updateResponse.weight());

        ExerciseSetResponse getResponse = exerciseSetService.getExerciseSet(addResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(addResponse.id(), getResponse.id());
        assertEquals(newReps, getResponse.reps());
        assertEquals(addResponse.weight(), getResponse.weight());
    }

    @Test
    void shouldUpdateOnlyExerciseSetWeight() {
        ExerciseSetResponse addResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);
        Double newWeight = 240.0;
        UpdateExerciseSetCommand updateCommand = new UpdateExerciseSetCommand(
                addResponse.id(),
                false,
                20,
                newWeight);

        ExerciseSetResponse updateResponse = exerciseSetService.updateExerciseSet(updateCommand);
        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(updateCommand.reps(), updateResponse.reps());
        assertEquals(newWeight, updateResponse.weight());

        ExerciseSetResponse getResponse = exerciseSetService.getExerciseSet(addResponse.id());
        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(updateResponse.id(), getResponse.id());
        assertEquals(updateCommand.reps(), getResponse.reps());
        assertEquals(newWeight, getResponse.weight());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingExerciseSet() {
        Long randomID = 1L;
        UpdateExerciseSetCommand updateCommand = new UpdateExerciseSetCommand(
                randomID,
                false,
                20,
                240.0);
        assertThrows(ExerciseSetNotFoundException.class,
                () -> exerciseSetService.updateExerciseSet(updateCommand));
    }

    @Test
    void shouldDeleteExistingExerciseSet() {
        ExerciseSetResponse addResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);
        exerciseSetService.deleteExerciseSet(addResponse.id());
        assertThrows(ExerciseSetNotFoundException.class, () -> exerciseSetService.getExerciseSet(addResponse.id()));
        assertEquals(0, exerciseSetService.getAllExerciseSets().size());
    }

    @Test
    void shouldThrowWhenGetNonExistingExerciseSet() {
        Long randomID = 1L;
        assertThrows(ExerciseSetNotFoundException.class, () -> exerciseSetService.getExerciseSet(randomID));
    }

    @Test
    void shouldReturnValidExerciseSetsWhenGetAllExerciseSets() {
        ExerciseSetResponse firstCreateResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);
        ExerciseSetResponse secondCreateResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);
        ExerciseSetResponse thirdCreateResponse = exerciseSetService.addExerciseSet(createCommand, exercisePlan);

        List<ExerciseSetResponse> getAllResponse = exerciseSetService.getAllExerciseSets();
        assertEquals(3, getAllResponse.size());
        assertEquals(firstCreateResponse.id(), getAllResponse.get(0).id());
        assertEquals(secondCreateResponse.id(), getAllResponse.get(1).id());
        assertEquals(thirdCreateResponse.id(), getAllResponse.get(2).id());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllExerciseSetsAndDatabaseIsEmpty() {
        List<ExerciseSetResponse> getAllResponse = exerciseSetService.getAllExerciseSets();
        assertEquals(0, getAllResponse.size());
    }

}
