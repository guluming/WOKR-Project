package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ObjectiveRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
