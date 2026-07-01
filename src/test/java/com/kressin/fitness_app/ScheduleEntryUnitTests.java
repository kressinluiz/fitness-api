package com.kressin.fitness_app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

@ExtendWith(MockitoExtension.class)
public class ScheduleEntryUnitTests {
    private ScheduleEntry scheduleEntry;
    private ZonedDateTime dateTime;
    private WorkoutDate workoutDate;

    @BeforeEach
    void setUp() {
        ScheduleType scheduleType = ScheduleType.RECURRING;
        Workout workout = new Workout(
                "Nome do Workout",
                "Descrição do Workout");

        WorkoutPlan workoutPlan = new WorkoutPlan(workout);
        workoutDate = new WorkoutDate(
                workoutPlan,
                scheduleType);
        dateTime = ZonedDateTime.now();
        scheduleEntry = new ScheduleEntry(
                1,
                dateTime,
                workoutDate);
    }

    @Test
    void shouldCreateWithValidArguments() {
        assertNotNull(scheduleEntry);
        assertEquals(1, scheduleEntry.getWeekDay());
        assertEquals(dateTime, scheduleEntry.getDateTime());
        assertEquals(workoutDate, scheduleEntry.getWorkoutDate());
    }

    @Test
    void shouldNotCreateWithInvalidWeekDay() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduleEntry(10, dateTime, workoutDate));
    }

    @Test
    void shouldNotCreateWithNullDateTime() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduleEntry(1, null, workoutDate));
    }

    @Test
    void shouldNotCreateWithNullWorkoutDate() {
        assertThrows(IllegalArgumentException.class, () -> new ScheduleEntry(1, dateTime, null));
    }

    @Test
    void shouldSetWithValidWeekDay() {
        scheduleEntry.setWeekDay(2);
        assertEquals(2, scheduleEntry.getWeekDay());
    }

    @Test
    void shouldNotSetWithInvalidWeekDay() {
        assertThrows(IllegalArgumentException.class, () -> scheduleEntry.setWeekDay(10));
    }

    @Test
    void shouldSetWithValidDateTime() {
        ZonedDateTime newDateTime = ZonedDateTime.now();
        scheduleEntry.setDateTime(newDateTime);
        assertEquals(newDateTime, scheduleEntry.getDateTime());
    }

    @Test
    void shouldNotSetWithNullDateTime() {
        assertThrows(IllegalArgumentException.class, () -> scheduleEntry.setDateTime(null));
    }
}
