package com.example.transpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fleet")
public class FleetController {

    @ModelAttribute("path")
    public String addServletPathToModel(HttpServletRequest request) {return request.getServletPath();}

    @GetMapping
    public String fleetHome() {return "user/fleet/fleet";}

    @GetMapping("/vehicles")
    public String vehiclesPage() {
        return "user/fleet/vehicles";
    }

    @GetMapping("/trailers")
    public String trailersPage() {
        return "user/fleet/trailers";
    }

    @GetMapping("/sets")
    public String setsPage() {
        return "user/fleet/sets";
    }
}
