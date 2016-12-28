package com.gameworld.app.service.impl;

import com.gameworld.app.domain.User;
import com.gameworld.app.repository.UserRepository;
import com.gameworld.app.security.SecurityUtils;
import com.gameworld.app.service.AdressService;
import com.gameworld.app.domain.Adress;
import com.gameworld.app.repository.AdressRepository;
import com.gameworld.app.repository.search.AdressSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Adress.
 */
@Service
@Transactional
public class AdressServiceImpl implements AdressService{

    private final Logger log = LoggerFactory.getLogger(AdressServiceImpl.class);

    @Inject
    private AdressRepository adressRepository;

    @Inject
    private AdressSearchRepository adressSearchRepository;

    @Inject
    private UserRepository userRepository;

    public Adress save(Adress adress) {
        log.debug("Request to save Adress : {}", adress);
        Adress result = adressRepository.save(adress);
        adressSearchRepository.save(result);
        return result;
    }

    @Transactional(readOnly = true)
    public Page<Adress> findAll(Pageable pageable) {
        log.debug("Request to get all Adresses");
        Page<Adress> result = adressRepository.findAll(pageable);
        return result;
    }

    @Transactional(readOnly = true)
    public List<Adress> findAllWhereGamerProfileIsNull() {
        log.debug("Request to get all adresses where GamerProfile is null");
        return StreamSupport
            .stream(adressRepository.findAll().spliterator(), false)
            .filter(adress -> adress.getGamerProfile() == null)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Adress findOne(Long id) {
        log.debug("Request to get Adress : {}", id);
        Adress adress = adressRepository.findOne(id);
        return adress;
    }

    public void delete(Long id) {
        log.debug("Request to delete Adress : {}", id);
        adressRepository.delete(id);
        adressSearchRepository.delete(id);
    }

    @Transactional(readOnly = true)
    public Page<Adress> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Adresses for query {}", query);
        Page<Adress> result = adressSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }

    @Override
    public Adress getUserAdress() {
        Adress adress = null;
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        if (user.isPresent()) {
            adress = adressRepository.getUserAddress(user.get().getGamerProfile().getId());
        }
        return adress;
    }
}
