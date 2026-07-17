package com.kressin.fitness_app.service.command;

import java.time.DayOfWeek;
import java.time.ZonedDateTime;

public record UpdateScheduleEntryCommand(
        Long id,
        Boolean shouldDelete,
        DayOfWeek weekDay,
        ZonedDateTime dateTime) {

}
