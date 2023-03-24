package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class ToDoDetailResponse {
    private boolean myToDo;
    private Long objectiveId;
    private Long keyResultId;
    private int krNumber;
    private Long toDoId;
    private String toDo;
    private String memo;
    private LocalDate startDate;
    private String startDateTime;
    private LocalDate endDate;
    private String endDateTime;
    private String fstartDate;
    private String fendDate;
    private int priority;

    @Builder
    public ToDoDetailResponse(boolean myToDo, Long objectiveId, Long keyResultId, int krNumber, Long toDoId, String toDo, String memo,
                              LocalDate startDate, String startDateTime, LocalDate endDate, String endDateTime,
                              String fstartDate, String fendDate, int priority) {
        this.myToDo = myToDo;
        this.objectiveId = objectiveId;
        this.keyResultId = keyResultId;
        this.krNumber = krNumber;
        this.toDoId = toDoId;
        this.toDo = toDo;
        this.memo = memo;
        this.startDate = startDate;
        this.startDateTime = startDateTime;
        this.endDate = endDate;
        this.endDateTime = endDateTime;
        this.fstartDate = fstartDate;
        this.fendDate = fendDate;
        this.priority = priority;
    }
}
