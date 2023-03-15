package com.slamdunk.WORK.Editor;

import lombok.Builder;
import lombok.Getter;

@Getter
public class KeyResultEditor {
    private String keyResult;
    private int progress;
    private int emoticon;

    @Builder
    public KeyResultEditor(String keyResult, int progress, int emoticon) {
        this.keyResult = keyResult;
        this.progress = progress;
        this.emoticon = emoticon;
    }
}
