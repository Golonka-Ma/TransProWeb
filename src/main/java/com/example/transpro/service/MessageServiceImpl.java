package com.example.transpro.service;

import com.example.transpro.model.Message;
import com.example.transpro.repository.MessageRepository;
import com.example.transpro.model.User;
import com.example.transpro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository,
                              UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesBetweenUsers(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("Nie znaleziono użytkowników o podanych nazwach");
        }

        return messageRepository.findMessagesBetweenUsers(user1.getId(), user2.getId());
    }

}
