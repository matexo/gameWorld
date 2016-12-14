package com.gameworld.app.repository;

import com.gameworld.app.domain.GameType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the GameType entity.
 */
@SuppressWarnings("unused")
public interface GameTypeRepository extends JpaRepository<GameType,Long> {

}
