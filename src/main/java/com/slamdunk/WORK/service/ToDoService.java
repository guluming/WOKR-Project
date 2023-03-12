package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.repository.ToDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {


    private final ToDoRepository toDoRepository;

    public List<ToDo> getAllToDos() {
        return toDoRepository.findAll();
    }

    public ResponseEntity<ToDo> getToDoById(Long toDo_id) {
        Optional<ToDo> toDoOptional = toDoRepository.findById(toDo_id);

        if (toDoOptional.isPresent()) {
            ToDo toDo = toDoOptional.get();
            return ResponseEntity.ok(toDo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ToDo createToDo(ToDoRequest toDoRequest) {
        ToDo newToDo = ToDo.builder()
                .toDo(toDoRequest.getToDo())
                .memo(toDoRequest.getMemo())
                .dDay(toDoRequest.getDDay())
                .priority(toDoRequest.getPriority())
                .build();
        return toDoRepository.save(newToDo);
    }



    public void updateToDo(Long toDo_id, ToDoRequest toDoRequest) {
        Optional<ToDo> toDoOptional = toDoRepository.findById(toDo_id);
        if (toDoOptional.isPresent()) {
            ToDo toDo = toDoOptional.get();
            toDo.setToDo(toDoRequest.getToDo());
            toDo.setMemo(toDoRequest.getMemo());
            toDo.setDDay(toDoRequest.getDDay());
            toDo.setPriority(toDoRequest.getPriority());
            toDoRepository.save(toDo);
        }
    }

    public void deleteToDoById(Long toDo_id) {
        toDoRepository.deleteById(toDo_id);
    }

    public void updateCompletion(Long toDo_id, ToDoRequest toDoRequest) {
        Optional<ToDo> toDoOptional = toDoRepository.findById(toDo_id);
        if (toDoOptional.isPresent()) {
            ToDo doneToDo = toDoOptional.get();
            doneToDo.setCompletion(toDoRequest.isCompletion());
            toDoRepository.save(doneToDo);



        }
    }
}


