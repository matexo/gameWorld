package com.gameworld.app.repository;

import com.gameworld.app.domain.Adress;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Adress entity.
 */
@SuppressWarnings("unused")
public interface AdressRepository extends JpaRepository<Adress,Long> {

    @Query(value = "SELECT adress " +
        "FROM GamerProfile gamerProfile " +
        "JOIN gamerProfile.adress adress " +
        "WHERE gamerProfile.id = :gamerProfileId ")
    Adress getUserAddress(@Param("gamerProfileId") Long gamerProfileId);
}
