package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoResponse {
    private boolean myToDo;
    private Long toDoId;
    private String toDo;
    private String memo;
    private LocalDate dDay;
    private int priority;
    private boolean completion;

    @Builder
    public ToDoResponse(
            Long toDoId, String toDo, String memo,
            LocalDate dDay, int priority,
            boolean completion) {

        this.toDoId = toDoId;
        this.toDo = toDo;
        this.memo = memo;
        this.dDay = dDay;
        this.priority = priority;
        this.completion = completion;

    }
}