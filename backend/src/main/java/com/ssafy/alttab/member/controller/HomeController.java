package com.ssafy.alttab.member.controller;

import com.ssafy.alttab.common.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@RequestParam String token, Model model) {
        System.out.println("HomeController.loginSuccess called with token: " + token);
        if (token != null && !token.isEmpty()) {
            String username = jwtUtil.extractUsername(token);
            model.addAttribute("name", username);
            model.addAttribute("token", token);
        } else {
            model.addAttribute("name", "Unknown User");
        }
        return "loginSuccess";
    }
}