package com.gameworld.app.repository;

import com.gameworld.app.domain.Platform;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Platform entity.
 */
@SuppressWarnings("unused")
public interface PlatformRepository extends JpaRepository<Platform,Long> {

}
