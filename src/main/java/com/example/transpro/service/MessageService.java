package com.example.transpro.service;

import com.example.transpro.model.Message;

import java.util.List;

public interface MessageService {
    Message save(Message message);

    List<Message> getMessagesBetweenUsers(String senderUsername, String receiverUsername);
}
