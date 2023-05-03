package com.slamdunk.WORK.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
@ToString
public class UserRequest {
    //이메일 형식
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$")
    @NotBlank
    private String email;

    //최소 1개의 영문 대소문자+숫자+특수문자를 포함하여 8~20자리이어야 합니다
    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)(?=.*\\W)).{8,16}$")
    @NotBlank
    private String password;

    @Pattern(regexp = "^(?=.*[a-zA-Z])((?=.*\\d)(?=.*\\W)).{8,16}$")
    @NotBlank
    private String confirmpassword;

    @Pattern(regexp = "^[가-힣a-zA-Z]{2,20}$")
    @NotBlank
    private String name;

    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$")
    @NotBlank
    private String team;

//    @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$")
//    @NotBlank
//    private String teamposition;
}
