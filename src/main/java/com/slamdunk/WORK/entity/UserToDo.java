package com.slamdunk.WORK.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@NoArgsConstructor
@Entity
public class UserToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserObjective_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_id")
    private Objective objective;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyResult_id")
    private KeyResult keyResult;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toDo_id")
    private ToDo toDo;
    @Column
    private boolean deleteState;

    public UserToDo(User user, Objective objective, KeyResult keyResult, ToDo toDo) {
        this.user = user;
        this.objective = objective;
        this.keyResult = keyResult;
        this.toDo = toDo;
    }
}
