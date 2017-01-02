package com.gameworld.app.repository;

import com.gameworld.app.domain.Conversation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Conversation entity.
 */
@SuppressWarnings("unused")
public interface ConversationRepository extends JpaRepository<Conversation,Long> {

    @Query(value = "SELECT con FROM Conversation con " +
        "JOIN FETCH con.profiles gp " +
        "JOIN FETCH con.profiles gp2 " +
        "WHERE gp.name = :gamerName " +
        "ORDER BY con.lastUpdate ",
    countQuery = "SELECT count(con) FROM Conversation con JOIN con.profiles gp JOIN con.profiles gp2 WHERE gp.name = :gamerName ORDER BY con.lastUpdate ")
    Page<Conversation> findAllByGamerName(@Param("gamerName") String gamerName , Pageable pageable);

    @Query(value = "SELECT con " +
        "FROM Conversation con " +
        "JOIN FETCH con.profiles gp " +
        "JOIN FETCH con.profiles gp2 " +
        "WHERE con.id = :id " +
        "AND (gp.name = :gamerName " +
        "OR gp2.name = :gamerName) ")
    Conversation findOneByGamerName(@Param("id") Long id , @Param("gamerName") String gamerName);

    @Query(value = "SELECT con " +
        "FROM Conversation con " +
        "JOIN FETCH con.profiles gp1 " +
        "JOIN FETCH con.profiles gp2 " +
        "WHERE gp1.id =  :senderId AND gp2.id = :receiverId " +
        "OR gp2.id = :senderId AND gp1.id = :receiverId ")
    Conversation findConversationBetweenProfiles(@Param("senderId") Long senderId , @Param("receiverId") Long receiverId);


}
