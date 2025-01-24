package com.example.transpro.repository;

import com.example.transpro.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "((m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1)) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findMessagesBetweenUsers(@Param("userId1") Long userId1,
                                           @Param("userId2") Long userId2);
}
