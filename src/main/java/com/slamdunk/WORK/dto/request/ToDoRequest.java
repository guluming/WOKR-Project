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
    private String startDate;
    private String endDate;
    private int priority;
    private boolean display;
    private boolean completion;
    private Long keyResultId;
}