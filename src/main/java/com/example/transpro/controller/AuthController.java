package com.example.transpro.controller;

import com.example.transpro.dto.UserRegistrationDto;
import com.example.transpro.model.User;
import com.example.transpro.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "public/register";
    }

    @GetMapping("/recovery")
    public String recovery() {
        return "public/passwordrecover";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {
        User existing = userService.findByUsername(userDto.getUsername());
        if (existing != null) {
            result.rejectValue("username", null, "Istnieje już użytkownik o takiej nazwie");
        }

        if (result.hasErrors()) {
            return "public/register";
        }

        userService.save(userDto);
        return "redirect:/register?success";
    }
}

