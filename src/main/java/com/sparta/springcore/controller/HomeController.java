package com.sparta.springcore.controller;

import com.sparta.springcore.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/") // @AuthenticationPrincipal -> 스프링 시큐리티가 로그인된 회원의 정보를 넘겨줌.
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        return "index"; //index를 리턴할 때 username을 포함. 이는 index에서 타임리프로 ${username}하면 연결됨!
    }

    @GetMapping("/admin")
    public String admin(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("admin", true);
        return "index";
    }
}