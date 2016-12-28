package com.gameworld.app.service;

import com.gameworld.app.domain.Adress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Adress.
 */
public interface AdressService {

    Adress save(Adress adress);
    Page<Adress> findAll(Pageable pageable);
    List<Adress> findAllWhereGamerProfileIsNull();
    Adress findOne(Long id);
    void delete(Long id);

    Page<Adress> search(String query, Pageable pageable);
    Adress getUserAdress();
}
