package com.slamdunk.WORK.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private boolean myInfo;
    private Long userId;
    private String email;
    private String name;
    private String team;
    private String teamposition;
    private boolean firstLogin;
}
