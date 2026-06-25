package com.kressin.fitness_app.service.command;

import java.time.ZonedDateTime;

public record UpdateScheduleEntryCommand(
    Long id,
    Integer weekDay,
    ZonedDateTime dateTime
) {
    
}
