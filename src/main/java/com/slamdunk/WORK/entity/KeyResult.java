package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.KeyResultRequest;
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
    @Column(unique = true)
    private String keyResult;
    @Column
    private int progress;
    @Column
    private int emoticon;

    public KeyResult(KeyResultRequest params) {
        this.keyResult = params.getKeyResult();
    }
}
