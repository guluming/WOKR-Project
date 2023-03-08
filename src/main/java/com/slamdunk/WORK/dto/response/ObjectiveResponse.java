package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ObjectiveResponse {
    private Long objectiveId;
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    private int color;
    private int progress;
}
