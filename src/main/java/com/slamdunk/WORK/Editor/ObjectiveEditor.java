package com.slamdunk.WORK.Editor;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ObjectiveEditor {
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    private String color;

    @Builder
    public ObjectiveEditor(String objective, LocalDate startdate, LocalDate enddate, String color) {
        this.objective = objective;
        this.startdate = startdate;
        this.enddate = enddate;
        this.color = color;
    }
}
