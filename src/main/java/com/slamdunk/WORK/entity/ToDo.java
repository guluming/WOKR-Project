package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ToDo extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "toDo_id")
    private Long id;

    @Column
    private String toDo;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private LocalDateTime registerDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int priority;


    @Column(nullable = false)
    private boolean display;

    @Column(nullable = false)
    private boolean completion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;


    public ToDo(ToDoRequest param){
        this.toDo = param.getToDo();
        this.memo = param.getMemo();
        this.startDate = param.getStartDate();
        this.endDate = param.getEndDate();
        this.priority = param.getPriority();
       // this.display = param.isDisplay();
        this.completion = false;

    }

        public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }
    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public void updateToDo(ToDoRequest toDoRequest) {

        this.toDo = toDoRequest.getToDo();
        this.memo = toDoRequest.getMemo();
        this.startDate = toDoRequest.getStartDate();
        this.endDate = toDoRequest.getEndDate();
        this.priority = toDoRequest.getPriority();
        //this.display = toDoRequest.isDisplay();

    }



}

