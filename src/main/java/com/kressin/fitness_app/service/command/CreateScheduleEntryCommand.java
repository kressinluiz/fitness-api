package com.kressin.fitness_app.service.command;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

public record CreateScheduleEntryCommand(
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {

}
