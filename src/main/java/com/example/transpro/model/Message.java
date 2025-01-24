package com.example.transpro.model;

import lombok.*;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime timestamp;

    @Transient
    private String receiverUsername;

    // Relacja z nadawcą
    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnoreProperties({"password", "email", "roles"})
    private User sender;

    // Relacja z odbiorcą (opcjonalnie dla wiadomości prywatnych)
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnoreProperties({"password", "email", "roles"})
    private User receiver;

    // Relacja z czatem grupowym (opcjonalnie)
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
}
