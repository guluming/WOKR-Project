package com.slamdunk.WORK.sort;

import com.slamdunk.WORK.entity.UserToDo;

import java.util.Comparator;

public class CreatedDateSort implements Comparator<UserToDo> {
    public int compare(UserToDo a, UserToDo b) {
        return a.getToDo().getCreatedDate().compareTo(b.getToDo().getCreatedDate());
    }
}
