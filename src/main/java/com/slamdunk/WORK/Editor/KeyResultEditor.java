package com.slamdunk.WORK.Editor;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KeyResultEditor {
    private String keyResult;
    private int progress;
    private int emoticon;
    private boolean deleteState;

    @Builder
    public KeyResultEditor(String keyResult, int progress, int emoticon, boolean deleteState) {
        this.keyResult = keyResult;
        this.progress = progress;
        this.emoticon = emoticon;
        this.deleteState =deleteState;
    }
}
