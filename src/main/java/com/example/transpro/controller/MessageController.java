package com.example.transpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MessageController {

    @ModelAttribute("path")
    public String addServletPathToModel(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping("/messenger")
    public String home() {
        return "user/messenger";
    }
}
