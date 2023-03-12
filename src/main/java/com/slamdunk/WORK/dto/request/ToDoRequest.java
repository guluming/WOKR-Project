package com.slamdunk.WORK.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ToDoRequest {
    private String toDo;
    private String memo;
    private LocalDate dDay;
    private int priority;
    private boolean completion;



}
