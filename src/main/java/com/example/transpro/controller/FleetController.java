package com.example.transpro.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fleet")
public class FleetController {

    @ModelAttribute("path")
    public String addServletPathToModel(HttpServletRequest request) {
        return request.getServletPath();
    }

    @GetMapping
    public String fleetHome() {
        // Perhaps return a general fleet overview page if needed
        return "user/fleet/fleet";
        // Or redirect to /fleet/vehicles
    }

    @GetMapping("/vehicles")
    public String vehiclesPage() {
        return "user/fleet/vehicles"; // This will load vehicles.html
    }

    @GetMapping("/trailers")
    public String trailersPage() {
        return "user/fleet/trailers"; // This will load trailers.html
    }

    @GetMapping("/sets")
    public String setsPage() {
        return "user/fleet/sets"; // If you have a sets.html for "zestawy"
    }
}
