package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class ExerciseUnitTests {
    @Test
    void shouldCreateWithValidArguments() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        assertNotNull(exercise);
        assertEquals("Exercício de Teste", exercise.getName());
        assertEquals("Descrição do Exercício de Teste", exercise.getDescription());
        assertEquals("Categoria do Exercício de Teste", exercise.getCategory());
        assertEquals("Grupo do Exercício de Teste", exercise.getMuscleGroup());
    }

    @Test
    void shouldNotCreateWithBlankName() {
        assertThrows(BusinessException.class, () -> new Exercise(
                "",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste"));
    }

    @Test
    void shouldNotCreateWithNullName() {
        assertThrows(BusinessException.class, () -> new Exercise(
                null,
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste"));
    }

    @Test
    void shouldNotCreateWithNullDescription() {
        assertThrows(BusinessException.class, () -> new Exercise(
                "Exercício de Teste",
                null,
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste"));
    }

    @Test
    void shouldNotCreateWithNullCategory() {
        assertThrows(BusinessException.class, () -> new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                null,
                "Grupo do Exercício de Teste"));
    }

    @Test
    void shouldNotCreateWithNullMuscleGroup() {
        assertThrows(BusinessException.class, () -> new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                null));
    }

    @Test
    void shouldUpdateName() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        exercise.setName("Novo Nome");
        assertEquals("Novo Nome", exercise.getName());
    }

    @Test
    void shouldNotUpdateWithNullName() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        assertThrows(BusinessException.class, () -> exercise.setName(null));
    }

    @Test
    void shouldNotUpdateWithBlankName() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        assertThrows(BusinessException.class, () -> exercise.setName(""));
    }

    @Test
    void shouldUpdateDescription() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        exercise.setDescription("Nova Descrição");
        assertEquals("Nova Descrição", exercise.getDescription());
    }

    @Test
    void shouldNotUpdateWithNullDescription() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        assertThrows(BusinessException.class, () -> exercise.setDescription(null));
    }

    @Test
    void shouldUpdateCategory() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        exercise.setCategory("Nova Categoria");
        assertEquals("Nova Categoria", exercise.getCategory());
    }

    @Test
    void shouldNotUpdateWithNullCategory() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        assertThrows(BusinessException.class, () -> exercise.setCategory(null));
    }

    @Test
    void shouldUpdateMuscleGroup() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        exercise.setMuscleGroup("Novo Grupo");
        assertEquals("Novo Grupo", exercise.getMuscleGroup());
    }

    @Test
    void shouldNotUpdateWithNullMuscleGroup() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        assertThrows(BusinessException.class, () -> exercise.setMuscleGroup(null));
    }

    @Test
    void shouldHaveValidExercisePlansList() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");
        assertNotEquals(null, exercise.getExercisePlans());
        assertEquals(true, exercise.getExercisePlans().isEmpty());
    }
}
