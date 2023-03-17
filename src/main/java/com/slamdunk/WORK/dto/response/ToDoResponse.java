package com.slamdunk.WORK.dto.response;

import com.slamdunk.WORK.entity.KeyResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToDoResponse {

    private boolean myToDo;
    private Long toDoId;
    private String toDo;
    private String memo;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int priority;
    private boolean display;
    private boolean completion;

    private KeyResult keyResult;


//    public ToDoResponse(boolean myToDo,Long toDoId,
//                        String toDo, String memo, LocalDateTime startDate,
//                        LocalDateTime endDate,int priority, boolean display ){
//        this.myToDo = myToDo;
//        this.toDoId = toDoId;
//        this.toDo = toDo;
//        this.memo =memo;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.priority =priority;
//        this.display = display;
//    }
//
//    public void ToDoResponseCom(boolean completion){
//        this.completion = completion;
//    }
//


}