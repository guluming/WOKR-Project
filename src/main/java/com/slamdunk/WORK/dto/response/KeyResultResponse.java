package com.slamdunk.WORK.dto.response;

import lombok.*;

@NoArgsConstructor
@Getter
public class KeyResultResponse {
    private boolean myKeyResult;
    private Long keyResultId;
    private int krNumber;
    private String keyResult;
    private int progress;
    private int emotion;
    private String color;

    @Builder
    public KeyResultResponse(boolean myKeyResult, Long keyResultId, int krNumber, String keyResult, int progress, int emotion, String color) {
        this.myKeyResult = myKeyResult;
        this.keyResultId = keyResultId;
        this.krNumber = krNumber;
        this.keyResult = keyResult;
        this.progress = progress;
        this.emotion = emotion;
        this.color = color;
    }
}
