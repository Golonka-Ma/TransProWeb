package com.example.transpro.rest;

import com.example.transpro.model.Message;
import com.example.transpro.service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;

    public MessageRestController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{receiverUsername}")
    public List<Message> getMessages(@PathVariable String receiverUsername, Principal principal) {
        return messageService.getMessagesBetweenUsers(principal.getName(), receiverUsername);
    }
}
