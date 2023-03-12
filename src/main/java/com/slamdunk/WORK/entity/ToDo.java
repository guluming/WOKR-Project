package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    private Long toDo_id;

    @Column(unique = true)
    private String toDo;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private LocalDate dDay;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private boolean completion;

    @Column(nullable = false)
    private boolean deleteState;


    public ToDo(ToDoRequest toDoRequest) {
        this.toDo=toDoRequest.getToDo();
        this.memo=toDoRequest.getMemo();
        this.dDay=toDoRequest.getDDay();
        this.priority=toDoRequest.getPriority();
    }


}