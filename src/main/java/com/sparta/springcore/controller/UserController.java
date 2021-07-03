package com.sparta.springcore.controller;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    //UserService를 Bean으로 주입받는 부분 추가. 13~19
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    } // 로그인 요청이 오면 "login"패이지로 이동함. 이게 어떻게 가능하냐면 타임리프 때문인데, "login"입력해주면, 타임리프의 루트인
    // resources/static에서 "login.html"을 찾아서 내려줌!!

    //로그인 에러 페이지
    @GetMapping("/user/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login"; // 로그인 에러 시에도 login.html을 내려주지만, 에러에 대한 표시가 되도록 설정.
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @PostMapping("/user/signup")
    public String registerUser(SignupRequestDto requestDto) {
        userService.registerUser(requestDto);
        return "redirect:/";
    }

    //인가받지 않은 사용자가 허용되지 않은 페이지 접근 시 아래를 리턴
    @GetMapping("/user/forbidden")
    public String forbidden() {
        return "forbidden";
    }

    //카카오로부터 오는 콜백을 처리하는 부분
    @GetMapping("/user/kakao/callback")
    public String kakaoLogin(String code) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드. == String code의 내용
        userService.kakaoLogin(code);

        return "redirect:/";
    }
}