package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyResult_id")
    private KeyResult keyResult;
    @Column(nullable = false)
    private String toDo;
    @Column
    private String memo;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column
    private String startDateTime;
    @Column(nullable = false)
    private LocalDate endDate;
    @Column
    private String endDateTime;
    @Column
    private int priority;
    @Column
    private boolean completion;
    @Column
    private boolean deleteState;

    public ToDo(ToDoRequest param, Objective objective, KeyResult keyResult){
        this.toDo = param.getToDo();
        this.memo = param.getMemo();
        this.startDate = param.getStartDate();
        this.startDateTime = param.getStartDateTime();
        this.endDate = param.getEndDate();
        this.endDateTime = param.getEndDateTime();
        this.priority = param.getPriority();
        this.objective = objective;
        this.keyResult = keyResult;
    }

//    public void updateToDo(ToDoRequest toDoRequest) {
//        this.toDo = toDoRequest.getToDo();
//        this.memo = toDoRequest.getMemo();
//        this.startDate = toDoRequest.getStartDate();
//        this.startDateTime = toDoRequest.getStartDateTime();
//        this.endDate = toDoRequest.getEndDate();
//        this.endDateTime = toDoRequest.getEndDateTime();
//        this.priority = toDoRequest.getPriority();
//    }
}

