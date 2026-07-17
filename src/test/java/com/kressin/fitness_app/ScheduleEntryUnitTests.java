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
                DayOfWeek.MONDAY,
                dateTime,
                workoutDate);
    }

    @Test
    void shouldCreateWithValidArguments() {
        assertNotNull(scheduleEntry);
        assertEquals(DayOfWeek.MONDAY, scheduleEntry.getWeekDay());
        assertEquals(dateTime, scheduleEntry.getDateTime());
        assertEquals(workoutDate, scheduleEntry.getWorkoutDate());
    }

    @Test
    void shouldNotCreateWithNullDateTime() {
        assertThrows(BusinessException.class, () -> new ScheduleEntry(DayOfWeek.MONDAY, null, workoutDate));
    }

    @Test
    void shouldNotCreateWithNullWorkoutDate() {
        assertThrows(BusinessException.class, () -> new ScheduleEntry(DayOfWeek.MONDAY, dateTime, null));
    }

    @Test
    void shouldSetWithValidWeekDay() {
        scheduleEntry.setWeekDay(DayOfWeek.TUESDAY);
        assertEquals(DayOfWeek.TUESDAY, scheduleEntry.getWeekDay());
    }

    @Test
    void shouldNotSetWithNullWeekDay() {
        assertThrows(BusinessException.class, () -> scheduleEntry.setWeekDay(null));
    }

    @Test
    void shouldSetWithValidDateTime() {
        ZonedDateTime newDateTime = ZonedDateTime.now();
        scheduleEntry.setDateTime(newDateTime);
        assertEquals(newDateTime, scheduleEntry.getDateTime());
    }

    @Test
    void shouldNotSetWithNullDateTime() {
        assertThrows(BusinessException.class, () -> scheduleEntry.setDateTime(null));
    }
}
