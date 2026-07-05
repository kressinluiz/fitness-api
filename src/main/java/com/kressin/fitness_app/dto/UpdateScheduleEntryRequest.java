package com.kressin.fitness_app.dto;

import java.time.ZonedDateTime;

public record UpdateScheduleEntryRequest(
        Long id,
        Boolean shouldDelete,
        Integer weekDay,
        ZonedDateTime dateTime) {

}
