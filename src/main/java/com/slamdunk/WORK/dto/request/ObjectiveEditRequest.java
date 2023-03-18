package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ObjectiveEditRequest {
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    private String color;
}
