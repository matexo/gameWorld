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

    @Query(value = "SELECT con FROM Conversation con JOIN FETCH con.profiles gp WHERE gp.name = :gamerName",
    countQuery = "SELECT count(con) FROM Conversation con JOIN con.profiles gp WHERE gp.name = :gamerName")
    Page<Conversation> findAllByGamerName(@Param("gamerName") String gamerName , Pageable pageable);

    @Query(value = "SELECT con " +
        "FROM Conversation con " +
        "JOIN FETCH con.profiles gp " +
        "WHERE con.id = :id " +
        "AND gp.name = :gamerName " +
        "ORDER BY con.lastUpdate ")
    Conversation findOneByGamerName(@Param("id") Long id , @Param("gamerName") String gamerName);

}
