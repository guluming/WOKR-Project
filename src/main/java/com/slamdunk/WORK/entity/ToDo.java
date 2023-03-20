package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.Editor.ToDoEditor;
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

   public void ToDoEdit(ToDoEditor toDoEditor) {
        toDo = toDoEditor.getToDo();
        memo = toDoEditor.getMemo();
        startDate = toDoEditor.getStartDate();
        startDateTime = toDoEditor.getStartDateTime();
        endDate = toDoEditor.getEndDate();
        endDateTime = toDoEditor.getEndDateTime();
        priority = toDoEditor.getPriority();
        completion = toDoEditor.isCompletion();
        deleteState = toDoEditor.isDeleteState();
   }

   public ToDoEditor.ToDoEditorBuilder ToDoToEditor() {
        return ToDoEditor
                .builder()
                .toDo(toDo)
                .memo(memo)
                .startDate(startDate)
                .startDateTime(startDateTime)
                .endDate(endDate)
                .endDateTime(endDateTime)
                .priority(priority)
                .completion(completion)
                .deleteState(deleteState);
   }
}

