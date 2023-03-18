package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Objective extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objective_id")
    private Long id;
    @Column(unique = true)
    private String objective;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column(nullable = false)
    private String color;
    @Column
    private int progress;

    public Objective(ObjectiveRequest param) {
        this.objective = param.getObjective();
        this.startDate = param.getStartdate();
        this.endDate = param.getEnddate();
        this.color = param.getColor();
        this.progress = 0;
    }

    public void objectiveProgressUpdate(int progress) {
        this.progress = progress;
    }
}
