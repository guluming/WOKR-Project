package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class Objective extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(unique = true)
    private String objective;
    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @Column(nullable = false)
    private int color;
    @Column
    private int progress;

    public Objective(ObjectiveRequest param) {
        this.objective = param.getObjective();
        this.startDate = param.getStartDate();
        this.endDate = param.getEndDate();
        this.color = param.getColor();
        this.progress = 0;
    }
}
