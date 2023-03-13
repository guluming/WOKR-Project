package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoResponse {

    private Long toDoId;
    private String toDo;
    private String memo;
    private LocalDateTime registerDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int priority;
    private boolean display;
    private boolean completion;
}