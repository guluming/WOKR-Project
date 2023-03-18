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
    private boolean myToDo;
    private Long toDoId;
    private String toDo;
    private String memo;
    private String startDate;
    private String endDate;
    private int priority;


}
