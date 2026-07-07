package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kressin.fitness_app.entity.ScheduleType;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanUnitTests {
    private Workout workout;
    private WorkoutPlan workoutPlan;

    @BeforeEach
    void setUp() {
        workout = new Workout(
                "Nome do Workout",
                "Descrição do Workout");

        workoutPlan = new WorkoutPlan(workout);
    }

    @Test
    void shouldCreateWithValidArguments() {
        assertNotNull(workoutPlan);
        assertEquals(workout, workoutPlan.getWorkout());
    }

    @Test
    void shouldNotCreateWithNullWorkout() {
        assertThrows(BusinessException.class, () -> new WorkoutPlan(null));
    }

    @Test
    void shouldSetWithValidWorkout() {
        Workout newWorkout = new Workout(
                "Nome do Workout",
                "Descrição do Workout");
        workoutPlan.setWorkout(newWorkout);
        assertEquals(newWorkout, workoutPlan.getWorkout());
    }

    @Test
    void shouldNotSetWithNullWorkout() {
        assertThrows(BusinessException.class, () -> workoutPlan.setWorkout(null));
    }

    @Test
    void shouldSetWithValidWorkoutDate() {
        WorkoutDate newWorkoutDate = new WorkoutDate(
                workoutPlan,
                ScheduleType.RECURRING);
        workoutPlan.setWorkoutDate(newWorkoutDate);
        assertEquals(newWorkoutDate, workoutPlan.getWorkoutDate());
    }

    @Test
    void shouldNotSetWithNullWorkoutDate() {
        assertThrows(BusinessException.class, () -> workoutPlan.setWorkoutDate(null));
    }
}
