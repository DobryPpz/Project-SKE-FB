package com.example.demo;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@AllArgsConstructor
@Controller
public class HomePageController {

    private UserService userService;
    private UserRepository userRepository;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @PostMapping("/registration?success")
    public String processRegister(User user, HttpServletRequest request)
            throws UnsupportedEncodingException {

        userService.register(user, getSiteURL(request));
        return "registration";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}