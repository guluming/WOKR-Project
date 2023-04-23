package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.TeamMemberToDoRequest;
import com.slamdunk.WORK.dto.request.ToDoEditRequest;
import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ToDoController {
    private final ToDoService toDoService;

    //할일 생성
    @PostMapping("/api/{objective_id}/{keyresult_id}/todo")
    public ResponseEntity<?> createToDo(
            @PathVariable("objective_id") Long objectiveId,
            @PathVariable("keyresult_id") Long keyResultId,
            @RequestBody ToDoRequest toDoRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return toDoService.createToDo(objectiveId, keyResultId, toDoRequest, userDetails);
    }

    //할일 전체 조회
    @GetMapping("/api/todo")
    public ResponseEntity<?> getAllToDos(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return toDoService.getAllToDos(userDetails);
    }

    //할일 상세 조회
    @GetMapping("/api/todo/detail/{todo_id}")
    public ResponseEntity<?> detailToDo(@PathVariable("todo_id") Long toDoId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return toDoService.detailToDo(toDoId, userDetails);
    }

    //할일 완료변경
    @PatchMapping("/api/todo/check/{todo_id}")
    public ResponseEntity<?> updateCompletion(@PathVariable("todo_id") Long todoId,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return toDoService.updateCompletion(todoId, userDetails);
    }

    //할일 수정
    @PatchMapping("/api/todo/{todo_id}")
    public ResponseEntity<?> updateToDo(@PathVariable("todo_id") Long todoId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @RequestBody ToDoEditRequest toDoEditRequest) {
        return toDoService.updateToDo(todoId, userDetails, toDoEditRequest);
    }

    //할일 삭제
    @DeleteMapping("/api/todo/{todo_id}")
    public ResponseEntity<String> toDoDelete(@PathVariable("todo_id") Long todoId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return toDoService.toDoDelete(todoId, userDetails);
    }

    //할일 기한만료 목록 조회
    @PostMapping("api/todo/expiration")
    public ResponseEntity<?> getExpirationToDo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestBody TeamMemberToDoRequest teamMemberToDoRequest) {
        if (teamMemberToDoRequest.getTeamMembers() == null || teamMemberToDoRequest.getTeamMembers().isEmpty()) {
            return new ResponseEntity<>("최소 1명의 사용자는 선택해야 합니다.", HttpStatus.BAD_REQUEST);
        } else {
            return toDoService.getExpirationToDo(userDetails, teamMemberToDoRequest);
        }
    }

    //할일 진행 목록 조회
    @PostMapping("api/todo/progress")
    public ResponseEntity<?> getProgressToDo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestBody TeamMemberToDoRequest teamMemberToDoRequest) {
        if (teamMemberToDoRequest.getTeamMembers() == null || teamMemberToDoRequest.getTeamMembers().isEmpty()) {
            return new ResponseEntity<>("최소 1명의 사용자는 선택해야 합니다.", HttpStatus.BAD_REQUEST);
        } else {
            return toDoService.getProgressToDo(userDetails);
        }
    }

    //할일 완료 목록 조회
    @PostMapping("api/todo/completion")
    public ResponseEntity<?> getCompletionToDo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestBody TeamMemberToDoRequest teamMemberToDoRequest){
        if (teamMemberToDoRequest.getTeamMembers() == null || teamMemberToDoRequest.getTeamMembers().isEmpty()) {
            return new ResponseEntity<>("최소 1명의 사용자는 선택해야 합니다.", HttpStatus.BAD_REQUEST);
        } else {
            return toDoService.getCompletionToDo(userDetails);
        }
    }

   //할일 대시보드 조회
    @GetMapping("api/todo/completion")
    public ResponseEntity<?> getDashToDo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return toDoService.getDashToDo(userDetails);
    }
}