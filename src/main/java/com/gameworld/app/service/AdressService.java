package com.gameworld.app.service;

import com.gameworld.app.domain.Adress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Adress.
 */
public interface AdressService {

    /**
     * Save a adress.
     *
     * @param adress the entity to save
     * @return the persisted entity
     */
    Adress save(Adress adress);

    /**
     *  Get all the adresses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Adress> findAll(Pageable pageable);
    /**
     *  Get all the AdressDTO where GamerProfile is null.
     *
     *  @return the list of entities
     */
    List<Adress> findAllWhereGamerProfileIsNull();

    /**
     *  Get the "id" adress.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Adress findOne(Long id);

    /**
     *  Delete the "id" adress.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the adress corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Adress> search(String query, Pageable pageable);
}
