package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoDetailResponse {
    private Long toDoId;
    private String toDo;
    private String memo;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int priority;
    private boolean display;
    private boolean completion;
}
