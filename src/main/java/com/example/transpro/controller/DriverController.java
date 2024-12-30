package com.example.transpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @ModelAttribute("path")
    public String addServletPathToModel(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping
    public String driverHome() {return "user/driver/drivers";}

    @GetMapping("/timeofwork")
    public String timeOfWork() {return "user/driver/timeofwork";}

    @GetMapping("/schedule")
    public String schedule() {return "user/driver/schedule";}
}
