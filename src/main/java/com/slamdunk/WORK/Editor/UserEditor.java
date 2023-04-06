package com.slamdunk.WORK.Editor;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserEditor {
    private String name;
    private String team;
    private String teamPosition;
    private boolean firstLogin;
    private boolean secessionState;

    @Builder
    public UserEditor(String name, String team, String teamPosition, boolean firstLogin, boolean secessionState) {
        this.name = name;
        this.team = team;
        this.teamPosition = teamPosition;
        this.firstLogin = firstLogin;
        this.secessionState = secessionState;
    }
}
