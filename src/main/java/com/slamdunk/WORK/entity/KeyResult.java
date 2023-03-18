package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.Editor.KeyResultEditor;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class KeyResult extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyResult_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;
    @Column(nullable = false)
    private String keyResult;
    @Column
    private int progress;
    @Column
    private int emoticon;
    @Column
    private boolean deleteState;

    public KeyResult(Objective objective, String keyResult) {
        this.objective = objective;
        this.keyResult = keyResult;
        this.progress = 0;
        this.emoticon = 0;
    }

    public void KeyResultEdit(KeyResultEditor keyResultEditor) {
        keyResult = keyResultEditor.getKeyResult();
        progress = keyResultEditor.getProgress();
        emoticon = keyResultEditor.getEmoticon();
    }

    public KeyResultEditor.KeyResultEditorBuilder KeyResultToEditor() {
        return KeyResultEditor
                .builder()
                .keyResult(keyResult)
                .progress(progress)
                .emoticon(emoticon);
    }
}
