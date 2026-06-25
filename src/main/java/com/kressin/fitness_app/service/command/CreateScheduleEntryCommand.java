package com.kressin.fitness_app.service.command;

import java.time.ZonedDateTime;

public record CreateScheduleEntryCommand(
        Integer weekDay,
        ZonedDateTime dateTime) {

}
