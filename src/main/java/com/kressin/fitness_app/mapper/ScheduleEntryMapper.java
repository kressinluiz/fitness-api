package com.kressin.fitness_app.mapper;

import java.util.List;

import com.kressin.fitness_app.dto.CreateScheduleEntryRequest;
import com.kressin.fitness_app.dto.ScheduleEntryResponse;
import com.kressin.fitness_app.dto.UpdateScheduleEntryRequest;
import com.kressin.fitness_app.entity.ScheduleEntry;
import com.kressin.fitness_app.service.command.CreateScheduleEntryCommand;
import com.kressin.fitness_app.service.command.UpdateScheduleEntryCommand;

public class ScheduleEntryMapper {
    public static CreateScheduleEntryCommand toCreateCommand(CreateScheduleEntryRequest request) {
        return new CreateScheduleEntryCommand(
                request.weekDay(),
                request.dateTime());
    }

    public static List<CreateScheduleEntryCommand> toCreateCommandList(List<CreateScheduleEntryRequest> requests) {
        return requests.stream().map(ScheduleEntryMapper::toCreateCommand).toList();
    }

    public static UpdateScheduleEntryCommand toUpdateCommand(UpdateScheduleEntryRequest request) {
        return new UpdateScheduleEntryCommand(
                request.id(),
                request.shouldDelete(),
                request.weekDay(),
                request.dateTime());
    }

    public static List<UpdateScheduleEntryCommand> toUpdateCommandList(List<UpdateScheduleEntryRequest> requests) {
        return requests.stream().map(ScheduleEntryMapper::toUpdateCommand).toList();
    }

    public static ScheduleEntryResponse toResponse(ScheduleEntry scheduleEntry) {
        return new ScheduleEntryResponse(
                scheduleEntry.getId(),
                scheduleEntry.getWeekDay(),
                scheduleEntry.getDateTime());
    }

    public static List<ScheduleEntryResponse> toResponseList(List<ScheduleEntry> scheduleEntries) {
        return scheduleEntries.stream().map(ScheduleEntryMapper::toResponse).toList();
    }

    public static CreateScheduleEntryCommand toCreateCommandFromUpdate(UpdateScheduleEntryCommand command) {
        return new CreateScheduleEntryCommand(
                command.weekDay(),
                command.dateTime());
    }
}
