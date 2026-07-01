package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.Workout;

@ExtendWith(MockitoExtension.class)
public class ExercisePlanUnitTests {
    @Test
    void shouldCreateWithValidArguments() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);

        assertNotNull(plan);
        assertEquals(exercise, plan.getExercise());
        assertEquals(workout, plan.getWorkout());
    }

    @Test
    void shouldNotCreateWithNullExercise() {
        Exercise exercise = null;

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        assertThrows(IllegalArgumentException.class, () -> new ExercisePlan(exercise, workout));
    }

    @Test
    void shouldNotCreateWithNullWorkout() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = null;

        assertThrows(IllegalArgumentException.class, () -> new ExercisePlan(exercise, workout));
    }

    @Test
    void shouldUpdateExercise() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        Exercise anotherExercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");
        assertNotEquals(exercise, anotherExercise);
        assertEquals(exercise, plan.getExercise());
        plan.setExercise(anotherExercise);
        assertEquals(anotherExercise, plan.getExercise());
    }

    @Test
    void shouldNotUpdateWithNullExercise() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        Exercise anotherExercise = null;
        assertThrows(IllegalArgumentException.class, () -> plan.setExercise(anotherExercise));
    }

    @Test
    void shouldUpdateWorkout() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        Workout anotherWorkout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");
        assertNotEquals(workout, anotherWorkout);
        assertEquals(workout, plan.getWorkout());
        plan.setWorkout(anotherWorkout);
        assertEquals(anotherWorkout, plan.getWorkout());
    }

    @Test
    void shouldNotUpdateWithNullWorkout() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        Workout anotherWorkout = null;
        assertThrows(IllegalArgumentException.class, () -> plan.setWorkout(anotherWorkout));
    }

    @Test
    void shouldHaveValidSetsList() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        assertNotEquals(null, plan.getSets());
        assertEquals(true, plan.getSets().isEmpty());
    }
}
