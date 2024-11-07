package com.example.transpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @GetMapping
    public String showDriversPage(Model model) {
        return "user/drivers"; // Path to drivers.html
    }
}
