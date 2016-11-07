package com.gameworld.app.repository;

import com.gameworld.app.domain.Adress;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Adress entity.
 */
@SuppressWarnings("unused")
public interface AdressRepository extends JpaRepository<Adress,Long> {

}
