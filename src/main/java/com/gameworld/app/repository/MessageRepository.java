package com.gameworld.app.repository;

import com.gameworld.app.domain.Message;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Message entity.
 */
@SuppressWarnings("unused")
public interface MessageRepository extends JpaRepository<Message,Long> {

    @Query("SELECT msg FROM Message msg JOIN msg.conversation con WHERE con.id = :conversationId ORDER BY msg.id DESC  ")
    List<Message> getLastMessage(@Param("conversationId") Long conversationId , Pageable pageable);

    default Set<Message> getLastMessage(Long conversationId) {
        return new HashSet<>(getLastMessage(conversationId , new PageRequest(0,1)));
    }
}
