package com.slamdunk.WORK.dto.response;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ObjectiveResponse {
    private boolean myObjective;
    private Long objectiveId;
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    private int color;
    private int progress;

    @Builder
    public ObjectiveResponse(
            boolean myObjective, Long objectiveId, String objective,
            LocalDate startdate, LocalDate enddate, int color,
            int progress) {
        this.myObjective = myObjective;
        this.objectiveId = objectiveId;
        this.objective = objective;
        this.startdate = startdate;
        this.enddate = enddate;
        this.color = color;
        this.progress = progress;
    }
}
