package com.slamdunk.WORK.dto.request;

import com.slamdunk.WORK.entity.ToDo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoRequest {
    private String toDo;
    private String memo;
    private LocalDate startDate;
    private String startDateTime;
    private LocalDate endDate;
    private String endDateTime;
    private int priority;
}