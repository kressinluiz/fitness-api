package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.entity.ScheduleType;
import com.kressin.fitness_app.entity.Workout;
import com.kressin.fitness_app.entity.WorkoutDate;
import com.kressin.fitness_app.entity.WorkoutPlan;
import com.kressin.fitness_app.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
public class WorkoutDateUnitTests {
    private WorkoutPlan workoutPlan;
    private WorkoutDate workoutDate;

    @BeforeEach
    void setUp() {
        ScheduleType scheduleType = ScheduleType.RECURRING;
        Workout workout = new Workout(
                "Nome do Workout",
                "Descrição do Workout");

        workoutPlan = new WorkoutPlan(workout);
        workoutDate = new WorkoutDate(
                workoutPlan,
                scheduleType);
    }

    @Test
    void shouldCreateWithValidArguments() {
        assertNotNull(workoutDate);
        assertEquals(ScheduleType.RECURRING, workoutDate.getScheduleType());
        assertEquals(workoutPlan, workoutDate.getWorkoutPlan());
    }

    @Test
    void shouldNotCreateWithInvalidWorkoutPlan() {
        assertThrows(BusinessException.class, () -> new WorkoutDate(
                null,
                ScheduleType.RECURRING));
    }

    @Test
    void shouldNotCreateWithInvalidScheduleType() {
        assertThrows(BusinessException.class, () -> new WorkoutDate(
                workoutPlan,
                null));
    }

    @Test
    void shouldAddValidScheduleEntry() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        ScheduleEntry entry = new ScheduleEntry(
                DayOfWeek.MONDAY,
                dateTime,
                workoutDate);
        assertNotNull(entry);
        assertEquals(dateTime, entry.getDateTime());
        assertEquals(DayOfWeek.MONDAY, entry.getWeekDay());
        assertEquals(workoutDate, entry.getWorkoutDate());
    }

    @Test
    void shouldNotAddNullScheduleEntry() {
        assertThrows(BusinessException.class, () -> workoutDate.addScheduleEntry(null));
    }

    @Test
    void shouldRemoveValidScheduleEntry() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        ScheduleEntry entry = new ScheduleEntry(
                DayOfWeek.MONDAY,
                dateTime,
                workoutDate);
        workoutDate.addScheduleEntry(entry);
        workoutDate.removeScheduleEntry(entry);
        assertEquals(0, workoutDate.getScheduleEntries().size());
    }

    @Test
    void shouldNotRemoveNullScheduleEntry() {
        assertThrows(BusinessException.class, () -> workoutDate.removeScheduleEntry(null));
    }

    @Test
    void shouldThrowIfRemovingScheduleEntryNotPresent() {
        WorkoutDate newWorkoutDate = new WorkoutDate(
                workoutPlan,
                ScheduleType.RECURRING);

        ZonedDateTime dateTime = ZonedDateTime.now();
        ScheduleEntry entry = new ScheduleEntry(
                DayOfWeek.MONDAY,
                dateTime,
                newWorkoutDate);
        assertThrows(BusinessException.class, () -> workoutDate.removeScheduleEntry(entry));
    }

    @Test
    void shouldSetWithValidScheduleType() {
        workoutDate.setScheduleType(ScheduleType.SPECIFIC_DATES);
        assertEquals(ScheduleType.SPECIFIC_DATES, workoutDate.getScheduleType());
    }

    @Test
    void shouldNotSetWithNullScheduleType() {
        assertThrows(BusinessException.class, () -> workoutDate.setScheduleType(null));
    }
}
