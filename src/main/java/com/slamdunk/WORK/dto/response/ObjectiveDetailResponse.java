package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ObjectiveDetailResponse {
    private boolean myObjective;
    private Long objectiveId;
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    private int color;
}
