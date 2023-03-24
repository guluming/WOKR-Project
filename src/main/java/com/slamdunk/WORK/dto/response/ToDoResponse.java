package com.slamdunk.WORK.dto.response;

import com.slamdunk.WORK.entity.KeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class ToDoResponse {
    private boolean myToDo;
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
    private boolean completion;
    private String color;

    @Builder
    public ToDoResponse(boolean myToDo, Long keyResultId, int krNumber, Long toDoId, String toDo, String memo,
                              LocalDate startDate, String startDateTime, LocalDate endDate, String endDateTime,
                              String fstartDate, String fendDate, int priority, boolean completion, String color ) {
        this.myToDo = myToDo;
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
        this.completion = completion;
        this.color = color;
    }

}