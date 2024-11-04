package com.example.transpro.model;
import lombok.*;
import jakarta.persistence.*;
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

    // Relacja z nadawcą
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    // Relacja z odbiorcą (opcjonalnie dla wiadomości prywatnych)
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    // Relacja z czatem grupowym (opcjonalnie)
    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;
}

