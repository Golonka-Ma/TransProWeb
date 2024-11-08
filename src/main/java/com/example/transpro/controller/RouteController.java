package com.example.transpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/routes")
public class RouteController {
    @ModelAttribute("path")
    public String addServletPathToModel(HttpServletRequest request) {
        return request.getServletPath();
    }
    @GetMapping
    public String viewMedUnits(Model model) {
        return "user/routes";
    }
}
