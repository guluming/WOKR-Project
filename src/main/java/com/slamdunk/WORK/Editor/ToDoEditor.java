package com.slamdunk.WORK.Editor;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ToDoEditor {
    private String toDo;
    private String memo;
    private LocalDate startDate;
    private String startDateTime;
    private LocalDate endDate;
    private String endDateTime;
    private int priority;
    private boolean completion;
    private boolean deleteState;

    @Builder
    public ToDoEditor(String toDo, String memo, LocalDate startDate, String startDateTime,
                      LocalDate endDate, String endDateTime, int priority, boolean completion, boolean deleteState) {
        this.toDo = toDo;
        this.memo = memo;
        this.startDate = startDate;
        this.startDateTime = startDateTime;
        this.endDate = endDate;
        this.endDateTime = endDateTime;
        this.priority = priority;
        this.completion = completion;
        this.deleteState = deleteState;
    }
}
