package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class ToDoExpirationResponse {
    private boolean myToDo;
    private Long userId;
    private String createUser;
    private List<expirationTodo> expirationTodo;
    @NoArgsConstructor
    @Getter
    public static class expirationTodo{
        private Long keyResultId;
        private int krNumber;
        private Long toDoId;
        private String toDo;
        private String memo;
        private LocalDate startDate;
        private String startDateTime;
        private LocalDate endDate;
        private String endDateTime;
        private String fstartDate;
        private String fendDate;
        private int priority;
        private boolean completion;
        private String color;

        @Builder
        public expirationTodo(Long keyResultId, int krNumber, Long toDoId, String toDo, String memo,
                                      LocalDate startDate, String startDateTime, LocalDate endDate, String endDateTime,
                                      String fstartDate, String fendDate, int priority, boolean completion, String color ) {
            this.keyResultId = keyResultId;
            this.krNumber = krNumber;
            this.toDoId = toDoId;
            this.toDo = toDo;
            this.memo = memo;
            this.startDate = startDate;
            this.startDateTime = startDateTime;
            this.endDate = endDate;
            this.endDateTime = endDateTime;
            this.fstartDate = fstartDate;
            this.fendDate = fendDate;
            this.priority = priority;
            this.completion = completion;
            this.color = color;
        }
    }

    @Builder
    public ToDoExpirationResponse(boolean myToDo, Long userId, String createUser, List<expirationTodo> expirationTodo) {
        this.myToDo = myToDo;
        this.userId = userId;
        this.createUser = createUser;
        this.expirationTodo = expirationTodo;
    }
}
