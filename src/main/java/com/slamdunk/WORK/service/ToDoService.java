package com.slamdunk.WORK.service;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.dto.response.ToDoDetailResponse;
import com.slamdunk.WORK.dto.response.ToDoResponse;
import com.slamdunk.WORK.entity.ToDo;
import com.slamdunk.WORK.repository.ToDoRepository;
import com.slamdunk.WORK.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ToDoService {


    private final ToDoRepository toDoRepository;

    public List<ToDo> getAllToDos(UserDetailsImpl userDetails) {
        return toDoRepository.findAll();
    }

    public ResponseEntity<?> detailToDo(Long todo_id, UserDetailsImpl userDetails) {
        Optional<ToDo> toDo = toDoRepository.findById(todo_id);

        if (toDo.isPresent()) {
            ToDoDetailResponse toDoDetailResponse = ToDoDetailResponse.builder()
                    .toDoId(toDo.get().getId())
                    .toDo(toDo.get().getToDo())
                    .memo(toDo.get().getMemo())
                    .startDate(toDo.get().getStartDate())
                    .endDate(toDo.get().getEndDate())
                    .priority(toDo.get().getPriority())
                    .display(toDo.get().isDisplay())
                    .completion(toDo.get().isCompletion())
                    .build();
            return new ResponseEntity<>(toDoDetailResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("존재하지 않는 투두입니다.", HttpStatus.BAD_REQUEST);
        }
    }



    public ResponseEntity<?> createToDo(ToDoRequest toDoRequest, UserDetailsImpl userDetails) {
        int priority = toDoRequest.getPriority();
        if (priority < 1 || priority > 4) {
            throw new IllegalArgumentException("Priority value must be between 1 and 4.");
        }

        ToDo newToDo = new ToDo(toDoRequest);
        newToDo.setRegisterDate(LocalDateTime.now());
        newToDo.setPriority(priority);
        toDoRepository.save(newToDo);

        ToDoResponse toDoResponse = ToDoResponse.builder()
                .toDoId(newToDo.getId())
                .toDo(newToDo.getToDo())
                .memo(newToDo.getMemo())
                .registerDate(newToDo.getRegisterDate())
                .startDate(newToDo.getStartDate())
                .endDate(newToDo.getEndDate())
                .priority(newToDo.getPriority())
                .display(newToDo.isDisplay())
                .build();
        return new ResponseEntity<>(toDoResponse, HttpStatus.CREATED);
    }

    public void updateToDo(Long todo_id, UserDetailsImpl userDetails, ToDoRequest toDoRequest) {
        int priority = toDoRequest.getPriority();
        if (priority < 1 || priority > 4) {
            throw new IllegalArgumentException("Priority value must be between 1 and 4.");
        }

        Optional<ToDo> toDoOptional = toDoRepository.findById(todo_id);
        if (toDoOptional.isPresent()) {
            ToDo existingToDo = toDoOptional.get();
            existingToDo.setToDo(toDoRequest.getToDo());
            existingToDo.setMemo(toDoRequest.getMemo());
            existingToDo.setStartDate(toDoRequest.getStartDate());
            existingToDo.setEndDate(toDoRequest.getEndDate());
            existingToDo.setPriority(toDoRequest.getPriority());
            existingToDo.setDisplay(toDoRequest.isDisplay());
            toDoRepository.save(existingToDo);
        }
    }

    public void deleteToDoById (Long todo_id, UserDetailsImpl userDetails){
        toDoRepository.deleteById(todo_id);
    }

    public void updateCompletion (Long todo_id, UserDetailsImpl userDetails, ToDoRequest toDoRequest){
        Optional<ToDo> toDoOptional = toDoRepository.findById(todo_id);
        if (toDoOptional.isPresent()) {
            ToDo donetoDo = toDoOptional.get();
            donetoDo.setCompletion(toDoRequest.isCompletion());
            toDoRepository.save(donetoDo);


        }
    }
}

