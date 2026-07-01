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
import com.kressin.fitness_app.entity.Workout;

@ExtendWith(MockitoExtension.class)
public class WorkoutUnitTests {
    private Workout workout;
    private String name;
    private String description;

    @BeforeEach
    void setUp() {
        name = "Workout de Teste";
        description = "Descrição de Teste";
        workout = new Workout(
                name,
                description);
    }

    @Test
    void shouldCreateWithValidArguments() {
        assertNotNull(workout);
        assertEquals(name, workout.getName());
        assertEquals(description, workout.getDescription());
    }

    @Test
    void shouldNotCreateWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Workout("", description));
    }

    @Test
    void shouldNotCreateWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Workout(null, description));
    }

    @Test
    void shouldNotCreateWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> new Workout(name, null));
    }

    @Test
    void shouldUpdateWithValidName() {
        String newName = "New Name";
        workout.setName(newName);

        assertEquals(newName, workout.getName());
    }

    @Test
    void shouldNotUpdateWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> workout.setName(""));
    }

    @Test
    void shouldNotUpdateWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> workout.setName(null));
    }

    @Test
    void shouldUpdateWithValidDescription() {
        String newDescription = "New Description";
        workout.setDescription(newDescription);

        assertEquals(newDescription, workout.getDescription());
    }

    @Test
    void shouldNotUpdateWithNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> workout.setDescription(null));
    }

    @Test
    void shouldAddValidExercisePlan() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        workout.addExercisePlan(plan);

        assertEquals(plan, workout.getExercisePlans().get(0));
    }

    @Test
    void shouldNotAddNullExercisePlan() {
        assertThrows(IllegalArgumentException.class, () -> workout.addExercisePlan(null));
    }

    @Test
    void shouldRemoveValidExercisePlan() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan plan = new ExercisePlan(exercise, workout);
        workout.addExercisePlan(plan);

        workout.removeExercisePlan(plan);
        assertEquals(0, workout.getExercisePlans().size());
    }

    @Test
    void shouldThrowDeletingNullExercisePlan() {
        assertThrows(IllegalArgumentException.class, () -> workout.removeExercisePlan(null));
    }

    @Test
    void shouldThrowDeletingExercisePlanNotInTheWorkout() {
        Exercise exercise = new Exercise(
                "Exercício de Teste",
                "Descrição do Exercício de Teste",
                "Categoria do Exercício de Teste",
                "Grupo do Exercício de Teste");

        Workout workout = new Workout(
                "Workout de Teste",
                "Descrição do Workout de Teste");

        ExercisePlan firstPlan = new ExercisePlan(exercise, workout);
        ExercisePlan secondPlan = new ExercisePlan(exercise, workout);

        workout.addExercisePlan(firstPlan);
        assertThrows(IllegalArgumentException.class, () -> workout.removeExercisePlan(secondPlan));
    }
}
