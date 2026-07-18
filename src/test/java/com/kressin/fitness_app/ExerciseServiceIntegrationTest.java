package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.kressin.fitness_app.dto.ExerciseResponse;
import com.kressin.fitness_app.exception.BusinessException;
import com.kressin.fitness_app.exception.ExerciseNotFoundException;
import com.kressin.fitness_app.service.ExerciseService;
import com.kressin.fitness_app.service.command.CreateExerciseCommand;
import com.kressin.fitness_app.service.command.UpdateExerciseCommand;

import jakarta.transaction.Transactional;

@Transactional
public class ExerciseServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    ExerciseService exerciseService;

    private String name;
    private String description;
    private String category;
    private String muscleGroup;
    Pageable pageable;
    String search;

    @BeforeEach
    void setUp() {
        name = "Nome do Exercício de Teste";
        description = "Descrição do Exercício de Teste";
        category = "Categoria do Exercício de Teste";
        muscleGroup = "Grupo do Exercício de Teste";
        pageable = PageRequest.of(0, 20);
        search = "";
    }

    @Test
    void shouldAddExerciseWithValidCommand() {
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse response = exerciseService.addExercise(validCommand);
        assertNotNull(response);
        assertNotNull(response.id());
        assertEquals(name, response.name());
        assertEquals(description, response.description());
        assertEquals(category, response.category());
        assertEquals(muscleGroup, response.muscleGroup());
    }

    @Test
    void shouldNotAddExerciseWithNullName() {
        CreateExerciseCommand invalidCommand = new CreateExerciseCommand(
                null,
                description,
                category,
                muscleGroup);
        assertThrows(BusinessException.class, () -> exerciseService.addExercise(invalidCommand));
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

    @Test
    void shouldNotAddExerciseWithBlankName() {
        CreateExerciseCommand invalidCommand = new CreateExerciseCommand(
                "",
                description,
                category,
                muscleGroup);
        assertThrows(BusinessException.class, () -> exerciseService.addExercise(invalidCommand));
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

    @Test
    void shouldNotAddExerciseWithNullDescription() {
        CreateExerciseCommand invalidCommand = new CreateExerciseCommand(
                name,
                null,
                category,
                muscleGroup);
        assertThrows(BusinessException.class, () -> exerciseService.addExercise(invalidCommand));
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

    @Test
    void shouldNotAddExerciseWithNullCategory() {
        CreateExerciseCommand invalidCommand = new CreateExerciseCommand(
                name,
                description,
                null,
                muscleGroup);
        assertThrows(BusinessException.class, () -> exerciseService.addExercise(invalidCommand));
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

    @Test
    void shouldNotAddExerciseWithNullMuscleGroup() {
        CreateExerciseCommand invalidCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                null);
        assertThrows(BusinessException.class, () -> exerciseService.addExercise(invalidCommand));
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

    @Test
    void shouldUpdateExerciseWithAllFields() {
        String newName = "Novo Nome do Exercício de Teste";
        String newDescription = "Novo Descrição do Exercício de Teste";
        String newCategory = "Novo Categoria do Exercício de Teste";
        String newMuscleGroup = "Novo Grupo do Exercício de Teste";
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        UpdateExerciseCommand updateCommand = new UpdateExerciseCommand(
                createResponse.id(),
                newName,
                newDescription,
                newCategory,
                newMuscleGroup);

        ExerciseResponse updateResponse = exerciseService.updateExercise(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newName, updateResponse.name());
        assertEquals(newDescription, updateResponse.description());
        assertEquals(newCategory, updateResponse.category());
        assertEquals(newMuscleGroup, updateResponse.muscleGroup());

        ExerciseResponse getResponse = exerciseService.getExercise(updateResponse.id());
        assertEquals(newName, getResponse.name());
        assertEquals(newDescription, getResponse.description());
        assertEquals(newCategory, getResponse.category());
        assertEquals(newMuscleGroup, getResponse.muscleGroup());
    }

    @Test
    void shouldUpdateOnlyExerciseName() {
        String newName = "Novo Nome do Exercício de Teste";
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        UpdateExerciseCommand updateCommand = new UpdateExerciseCommand(
                createResponse.id(),
                newName,
                null,
                null,
                null);

        ExerciseResponse updateResponse = exerciseService.updateExercise(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(newName, updateResponse.name());
        assertEquals(createResponse.description(), updateResponse.description());
        assertEquals(createResponse.category(), updateResponse.category());
        assertEquals(createResponse.muscleGroup(), updateResponse.muscleGroup());

        ExerciseResponse getResponse = exerciseService.getExercise(updateResponse.id());
        assertEquals(newName, getResponse.name());
        assertEquals(updateResponse.description(), getResponse.description());
        assertEquals(updateResponse.category(), getResponse.category());
        assertEquals(updateResponse.muscleGroup(), getResponse.muscleGroup());
    }

    @Test
    void shouldUpdateOnlyDescription() {
        String newDescription = "Novo Descrição do Exercício de Teste";
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        UpdateExerciseCommand updateCommand = new UpdateExerciseCommand(
                createResponse.id(),
                null,
                newDescription,
                null,
                null);

        ExerciseResponse updateResponse = exerciseService.updateExercise(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.name(), updateResponse.name());
        assertEquals(newDescription, updateResponse.description());
        assertEquals(createResponse.category(), updateResponse.category());
        assertEquals(createResponse.muscleGroup(), updateResponse.muscleGroup());

        ExerciseResponse getResponse = exerciseService.getExercise(updateResponse.id());
        assertEquals(updateResponse.name(), getResponse.name());
        assertEquals(newDescription, getResponse.description());
        assertEquals(updateResponse.category(), getResponse.category());
        assertEquals(updateResponse.muscleGroup(), getResponse.muscleGroup());
    }

    @Test
    void shouldUpdateOnlyCategory() {
        String newCategory = "Novo Categoria do Exercício de Teste";
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        UpdateExerciseCommand updateCommand = new UpdateExerciseCommand(
                createResponse.id(),
                null,
                null,
                newCategory,
                null);

        ExerciseResponse updateResponse = exerciseService.updateExercise(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.name(), updateResponse.name());
        assertEquals(createResponse.description(), updateResponse.description());
        assertEquals(newCategory, updateResponse.category());
        assertEquals(createResponse.muscleGroup(), updateResponse.muscleGroup());

        ExerciseResponse getResponse = exerciseService.getExercise(updateResponse.id());
        assertEquals(updateResponse.name(), getResponse.name());
        assertEquals(updateResponse.description(), getResponse.description());
        assertEquals(newCategory, getResponse.category());
        assertEquals(updateResponse.muscleGroup(), getResponse.muscleGroup());
    }

    @Test
    void shouldUpdateOnlyMuscleGroup() {
        String newMuscleGroup = "Novo Grupo do Exercício de Teste";
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        UpdateExerciseCommand updateCommand = new UpdateExerciseCommand(
                createResponse.id(),
                null,
                null,
                null,
                newMuscleGroup);

        ExerciseResponse updateResponse = exerciseService.updateExercise(updateCommand);

        assertNotNull(updateResponse);
        assertNotNull(updateResponse.id());
        assertEquals(createResponse.name(), updateResponse.name());
        assertEquals(createResponse.description(), updateResponse.description());
        assertEquals(createResponse.category(), updateResponse.category());
        assertEquals(newMuscleGroup, updateResponse.muscleGroup());

        ExerciseResponse getResponse = exerciseService.getExercise(updateResponse.id());
        assertEquals(updateResponse.name(), getResponse.name());
        assertEquals(updateResponse.description(), getResponse.description());
        assertEquals(updateResponse.category(), getResponse.category());
        assertEquals(newMuscleGroup, getResponse.muscleGroup());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingExercise() {
        Long randomID = 1L;
        String newName = "Novo Nome do Exercício de Teste";
        String newDescription = "Novo Descrição do Exercício de Teste";
        String newCategory = "Novo Categoria do Exercício de Teste";
        String newMuscleGroup = "Novo Grupo do Exercício de Teste";

        UpdateExerciseCommand updateCommand = new UpdateExerciseCommand(
                randomID,
                newName,
                newDescription,
                newCategory,
                newMuscleGroup);

        assertThrows(ExerciseNotFoundException.class,
                () -> exerciseService.updateExercise(updateCommand));
    }

    @Test
    void shouldDeleteExistingExercise() {
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        exerciseService.deleteExercise(createResponse.id());

        assertThrows(ExerciseNotFoundException.class,
                () -> exerciseService.getExercise(createResponse.id()));
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

    @Test
    void shouldGetExistingExercise() {
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse createResponse = exerciseService.addExercise(validCommand);

        ExerciseResponse getResponse = exerciseService.getExercise(createResponse.id());

        assertNotNull(getResponse);
        assertNotNull(getResponse.id());
        assertEquals(createResponse.id(), getResponse.id());
        assertEquals(createResponse.name(), getResponse.name());
        assertEquals(createResponse.description(), getResponse.description());
        assertEquals(createResponse.category(), getResponse.category());
        assertEquals(createResponse.muscleGroup(), getResponse.muscleGroup());
    }

    @Test
    void shouldThrowWhenGetNonExistingExercise() {
        Long randomID = 1L;
        assertThrows(ExerciseNotFoundException.class,
                () -> exerciseService.getExercise(randomID));
    }

    @Test
    void shouldReturnValidExercisesWhenGetAllExercises() {
        CreateExerciseCommand validCommand = new CreateExerciseCommand(
                name,
                description,
                category,
                muscleGroup);

        ExerciseResponse firstCreateResponse = exerciseService.addExercise(validCommand);
        ExerciseResponse secondCreateResponse = exerciseService.addExercise(validCommand);
        ExerciseResponse thirdCreateResponse = exerciseService.addExercise(validCommand);

        Page<ExerciseResponse> getAllResponse = exerciseService.getAllExercises(pageable, search);
        assertEquals(3, getAllResponse.getTotalElements());
        assertEquals(firstCreateResponse.id(), getAllResponse.getContent().get(0).id());
        assertEquals(secondCreateResponse.id(), getAllResponse.getContent().get(1).id());
        assertEquals(thirdCreateResponse.id(), getAllResponse.getContent().get(2).id());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllExercisesAndDatabseIsEmpty() {
        assertEquals(0, exerciseService.getAllExercises(pageable, search).getTotalElements());
    }

}
