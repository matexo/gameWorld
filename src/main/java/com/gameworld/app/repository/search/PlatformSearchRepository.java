package com.gameworld.app.repository.search;

import com.gameworld.app.domain.Platform;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Platform entity.
 */
public interface PlatformSearchRepository extends ElasticsearchRepository<Platform, Long> {
}
