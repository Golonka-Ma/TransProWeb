package com.example.transpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class SettingController {

    @ModelAttribute("path")
    public String addServletPathToModel(HttpServletRequest request) {
        return request.getServletPath();
    }
    @GetMapping("/settings")
    public String home() {
        return "user/settings";
    }
}

