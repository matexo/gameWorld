package com.gameworld.app.repository.search;

import com.gameworld.app.domain.Adress;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Adress entity.
 */
public interface AdressSearchRepository extends ElasticsearchRepository<Adress, Long> {
}
