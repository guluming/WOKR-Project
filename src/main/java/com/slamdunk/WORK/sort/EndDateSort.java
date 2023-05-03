package com.slamdunk.WORK.sort;

import com.slamdunk.WORK.entity.UserToDo;

import java.util.Comparator;

public class EndDateSort implements Comparator<UserToDo> {
    public int compare(UserToDo a, UserToDo b) {
        return a.getToDo().getEndDate().compareTo(b.getToDo().getEndDate());
    }
}
