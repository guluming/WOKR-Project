package com.slamdunk.WORK.dto.response;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class KeyResultResponse {
    private Long keyResultId;
    private String keyResult;
    private int progress;
    private int emotion;

    @Builder
    public KeyResultResponse(Long keyResultId, String keyResult, int progress, int emotion) {
        this.keyResultId = keyResultId;
        this.keyResult = keyResult;
        this.progress = progress;
        this.emotion = emotion;
    }
}
