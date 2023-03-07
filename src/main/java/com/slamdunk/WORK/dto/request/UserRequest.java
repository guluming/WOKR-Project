package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserRequest {
    private String email;
    private String password;
    private String confirmpassword;
    private String team;
    private String teamposition;
}
