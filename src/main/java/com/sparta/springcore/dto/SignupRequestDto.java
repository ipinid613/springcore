package com.sparta.springcore.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private boolean admin = false;
    //관리자 체크박스를 의미함. 체크박스 해제하면 false.
    private String adminToken = "";
}