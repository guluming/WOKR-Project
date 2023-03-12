package com.slamdunk.WORK.controller;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RequiredArgsConstructor
@RestController
public class ToDoController {


    private final ToDoService toDoService;
    //투두 전체 조회
    @GetMapping("/api/todo")
    public List<ToDo> getAllToDos() {
        return toDoService.getAllToDos();
    }

    //투두 상세 조회
    @GetMapping("/api/todo/detail/{toDo_id}")
    public ResponseEntity<?> getToDoById(@PathVariable Long toDo_id) {
        return toDoService.getToDoById(toDo_id);
    }

    //투두 생성
    @PostMapping("/api/todo")
    public ResponseEntity<?> createToDo(@RequestBody ToDoRequest toDoRequest) {
        ToDo newToDo = toDoService.createToDo(toDoRequest);
        return ResponseEntity.ok(newToDo);
    }


    //투두 수정
    @PatchMapping("/api/todo/{toDo_id}")
    public ResponseEntity<?> updateToDo(@PathVariable Long toDo_id, @RequestBody ToDoRequest toDoRequest) {
        toDoService.updateToDo(toDo_id, toDoRequest);
        return ResponseEntity.ok().build();
    }
    //투두 삭제
    @PatchMapping("/api/todo/secession/{toDo_id}")
    public ResponseEntity<?> deleteToDoById(@PathVariable Long toDo_id) {
        toDoService.deleteToDoById(toDo_id);
        return ResponseEntity.ok().build();
    }
    //투두 완료변경
    @PatchMapping("/api/todo/check/{todo_id}")
    public ResponseEntity<?> updateCompletion(@PathVariable Long todo_id,@RequestBody ToDoRequest toDoRequest) {
        toDoService.updateCompletion(todo_id, toDoRequest);
        return ResponseEntity.ok().build();
    }
}