package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kressin.fitness_app.entity.Exercise;
import com.kressin.fitness_app.entity.ExercisePlan;
import com.kressin.fitness_app.entity.ExerciseSet;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class ExerciseSetUnitTests {
    private Exercise exercise;
    private Workout workout;
    private ExerciseSet set;
    private Integer reps;
    private Double weight;
    private ExercisePlan plan;

    @BeforeEach
    void setUp() {
        reps = 10;
        weight = 10.0;
        exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");
        plan = new ExercisePlan(exercise, workout);
        set = new ExerciseSet(reps, weight, plan);
    }

    @Test
    void shouldCreateWithValidArguments() {
        assertNotNull(set);
        assertEquals(reps, set.getReps());
        assertEquals(weight, set.getWeight());
    }

    @Test
    void shouldNotCreateWithInvalidReps() {
        reps = 0;
        assertThrows(BusinessException.class, () -> new ExerciseSet(
                reps,
                weight,
                plan));
    }

    @Test
    void shouldNotCreateWithInvalidWeight() {
        weight = -0.1;
        assertThrows(BusinessException.class, () -> new ExerciseSet(
                reps,
                weight,
                plan));
    }

    @Test
    void shouldNotCreateWithInvalidExercisePlan() {
        plan = null;
        assertThrows(BusinessException.class, () -> new ExerciseSet(
                reps,
                weight,
                plan));
    }

    @Test
    void shouldUpdateReps() {
        Integer newReps = 20;
        set.setReps(newReps);
        assertEquals(newReps, set.getReps());
    }

    @Test
    void shouldNotUpdateWithInvalidReps() {
        reps = -1;
        assertThrows(BusinessException.class, () -> set.setReps(reps));
    }

    @Test
    void shouldUpdateWeight() {
        Double newWeight = 20.0;
        set.setWeight(newWeight);
        assertEquals(newWeight, set.getWeight());
    }

    @Test
    void shouldNotUpdateWithInvalidWeight() {
        weight = -1.0;
        assertThrows(BusinessException.class, () -> set.setWeight(weight));
    }
}
