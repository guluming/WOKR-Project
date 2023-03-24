package com.slamdunk.WORK.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
public class ToDoProgressResponse {
    private String targetDate;
    private List<progressTodo> progressTodo;
    private List<completionTodo> completionTodo;
    @NoArgsConstructor
    @Getter
    public static class progressTodo{
        private boolean myToDo;
        private String createUser;
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
        public progressTodo(boolean myToDo, String createUser, Long keyResultId, int krNumber, Long toDoId, String toDo, String memo,
                            LocalDate startDate, String startDateTime, LocalDate endDate, String endDateTime,
                            String fstartDate, String fendDate, int priority, boolean completion, String color ) {
            this.myToDo = myToDo;
            this.createUser = createUser;
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

    @NoArgsConstructor
    @Getter
    public static class completionTodo{
        private boolean myToDo;
        private String createUser;
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
        public completionTodo(boolean myToDo, String createUser, Long keyResultId, int krNumber, Long toDoId, String toDo, String memo,
                            LocalDate startDate, String startDateTime, LocalDate endDate, String endDateTime,
                            String fstartDate, String fendDate, int priority, boolean completion, String color ) {
            this.myToDo = myToDo;
            this.createUser = createUser;
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
    public ToDoProgressResponse(String targetDate, List<progressTodo> progressTodo, List<completionTodo> completionTodo) {
        this.targetDate = targetDate;
        this.progressTodo = progressTodo;
        this.completionTodo = completionTodo;
    }
}
