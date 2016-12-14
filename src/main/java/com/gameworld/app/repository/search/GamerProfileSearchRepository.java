package com.gameworld.app.repository.search;

import com.gameworld.app.domain.GamerProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GamerProfile entity.
 */
public interface GamerProfileSearchRepository extends ElasticsearchRepository<GamerProfile, Long> {
}
