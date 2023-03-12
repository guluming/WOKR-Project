package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class ToDoDetailResponse {
    private boolean myToDo;
    private Long toDoId;
    private String toDo;

    @Builder
    public ToDoDetailResponse(boolean myToDo, Long toDoId, String toDo) {
        this.myToDo = myToDo;
        this.toDoId = toDoId;
        this.toDo = toDo;
    }
}