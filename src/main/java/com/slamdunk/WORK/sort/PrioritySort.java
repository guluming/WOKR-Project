package com.slamdunk.WORK.sort;

import com.slamdunk.WORK.entity.UserToDo;

import java.util.Comparator;

public class PrioritySort implements Comparator<UserToDo> {
    public int compare(UserToDo a, UserToDo b) {
        return a.getToDo().getPriority() -b.getToDo().getPriority();
    }
}
