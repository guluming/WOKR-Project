package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.*;

import javax.persistence.*;
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
    @Column
    private String toDo;
    @Column(nullable = false)
    private String memo;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @Column
    private int priority;
    @Column(nullable = false)
    private boolean display;
    @Column(nullable = false)
    private boolean completion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "key_result_id")
    private KeyResult keyResult;



    public ToDo(ToDoRequest param){
        this.toDo = param.getToDo();
        this.memo = param.getMemo();
        this.startDate =LocalDateTime.parse(param.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.endDate = LocalDateTime.parse(param.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.priority = param.getPriority();
        this.display = param.isDisplay();
        this.completion = false;


    }

    public void setKeyResult(KeyResult keyResult) {
        this.keyResult = keyResult;
    }


    public void updateToDo(ToDoRequest toDoRequest) {

        this.toDo = toDoRequest.getToDo();
        this.memo = toDoRequest.getMemo();
        this.startDate = LocalDateTime.parse(toDoRequest.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.endDate =  LocalDateTime.parse(toDoRequest.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.priority = toDoRequest.getPriority();
        this.display = toDoRequest.isDisplay();

    }


}

