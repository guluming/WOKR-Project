package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.security.UserDetailsImpl;
import com.slamdunk.WORK.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ToDoController {

    private final ToDoService toDoService;


    //투두 전체 조회
    @GetMapping("/api/todo")
    public List<ToDo> getAllToDos(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return toDoService.getAllToDos(userDetails);
    }

    //투두 상세 조회
    @GetMapping("/api/todo/detail/{todo_id}")
    public ResponseEntity<?> detailToDo(@PathVariable("todo_id") Long toDoId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return toDoService.detailToDo(toDoId,userDetails);
    }


    //투두 생성
    @PostMapping("/api/todo")
    public ResponseEntity<?> createToDo(@RequestBody ToDoRequest toDoRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseEntity<?> createdToDo = toDoService.createToDo(toDoRequest, userDetails);
        return ResponseEntity.ok(createdToDo);
    }

    //투두 수정
    @PatchMapping("/api/todo/{todo_id}")
    public ResponseEntity<Void> updateToDo(@PathVariable("todo_id") Long todo_id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestBody ToDoRequest toDoRequest) {
        toDoService.updateToDo(todo_id,userDetails,toDoRequest);
        return ResponseEntity.ok().build();
    }


    //투두 삭제
    @PatchMapping("/api/todo/secession/{todo_id}")
    public ResponseEntity<?> deleteToDoById(@PathVariable("todo_id") Long todo_id,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        toDoService.deleteToDoById(todo_id,userDetails);
        return ResponseEntity.ok().build();
    }
    //투두 완료변경
    @PatchMapping("/api/todo/check/{todo_id}")
    public ResponseEntity<?> updateCompletion(@PathVariable("todo_id") Long todo_id,
                                              @AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody ToDoRequest toDoRequest) {
        toDoService.updateCompletion(todo_id, userDetails ,toDoRequest);
        return ResponseEntity.ok().build();
    }
}