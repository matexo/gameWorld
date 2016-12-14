package com.gameworld.app.repository.search;

import com.gameworld.app.domain.MarketOffer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MarketOffer entity.
 */
public interface MarketOfferSearchRepository extends ElasticsearchRepository<MarketOffer, Long> {
}
