package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KeyResultDetailResponse {
    private boolean myKeyResult;
    private Long keyResultId;
    private int krNumber;
    private String keyResult;

    @Builder
    public KeyResultDetailResponse(boolean myKeyResult, Long keyResultId, int krNumber, String keyResult) {
        this.myKeyResult = myKeyResult;
        this.keyResultId = keyResultId;
        this.krNumber =krNumber;
        this.keyResult = keyResult;
    }
}
