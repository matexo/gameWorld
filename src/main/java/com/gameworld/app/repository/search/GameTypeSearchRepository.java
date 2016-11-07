package com.gameworld.app.repository.search;

import com.gameworld.app.domain.GameType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the GameType entity.
 */
public interface GameTypeSearchRepository extends ElasticsearchRepository<GameType, Long> {
}
