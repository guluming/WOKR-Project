package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class OKRResponse {
    private boolean myObjective;
    private Long objectiveId;
    private String objective;
    private LocalDate startdate;
    private LocalDate enddate;
    private String color;
    private int progress;
    private List<keyResult> keyresult;

    @NoArgsConstructor
    @Getter
    public static class keyResult{
        private boolean myKeyResult;
        private Long keyResultId;
        private String keyResult;
        private int progress;
        private int emotion;

        @Builder
        public keyResult(boolean myKeyResult, Long keyResultId, String keyResult, int progress, int emotion) {
            this.myKeyResult = myKeyResult;
            this.keyResultId = keyResultId;
            this.keyResult = keyResult;
            this.progress = progress;
            this.emotion = emotion;
        }
    }

    @Builder
    public OKRResponse(
            boolean myObjective, Long objectiveId, String objective,
            LocalDate startdate, LocalDate enddate, String color,
            int progress, List<keyResult> keyresult) {
        this.myObjective = myObjective;
        this.objectiveId = objectiveId;
        this.objective = objective;
        this.startdate = startdate;
        this.enddate = enddate;
        this.color = color;
        this.progress = progress;
        this.keyresult = keyresult;
    }
}
