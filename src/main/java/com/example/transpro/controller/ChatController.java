package com.example.transpro.controller;

import com.example.transpro.model.Message;
import com.example.transpro.model.User;
import com.example.transpro.service.MessageService;
import com.example.transpro.service.UserService;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService messageService, UserService userService, SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload Message message, Principal principal) {
        // Ustaw nadawcę
        String senderUsername = principal.getName();
        User sender = userService.findByUsername(senderUsername);
        message.setSender(sender);

        // Ustaw odbiorcę
        String receiverUsername = message.getReceiverUsername();
        User receiver = userService.findByUsername(receiverUsername);
        message.setReceiver(receiver);

        // Ustaw znacznik czasu
        message.setTimestamp(LocalDateTime.now());

        // Zapisz wiadomość w bazie danych
        messageService.save(message);

        // Wyślij wiadomość do odbiorcy
        messagingTemplate.convertAndSendToUser(receiverUsername, "/queue/messages", message);

        // Opcjonalnie wyślij wiadomość do nadawcy, aby odświeżyć czat
        messagingTemplate.convertAndSendToUser(senderUsername, "/queue/messages", message);
    }
}
