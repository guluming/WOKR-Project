package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ToDoEditRequest {
    private String toDo;
    private String memo;
    private LocalDate startDate;
    private String startDateTime;
    private LocalDate endDate;
    private String endDateTime;
    private int priority;
}
