package com.gameworld.app.repository;

import com.gameworld.app.domain.GamerProfile;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the GamerProfile entity.
 */
@SuppressWarnings("unused")
public interface GamerProfileRepository extends JpaRepository<GamerProfile,Long> {

    @Query("select distinct gamerProfile from GamerProfile gamerProfile left join fetch gamerProfile.searchedGames left join fetch gamerProfile.conversations")
    List<GamerProfile> findAllWithEagerRelationships();

    @Query("select gamerProfile from GamerProfile gamerProfile left join fetch gamerProfile.searchedGames left join fetch gamerProfile.conversations where gamerProfile.id =:id")
    GamerProfile findOneWithEagerRelationships(@Param("id") Long id);

}
