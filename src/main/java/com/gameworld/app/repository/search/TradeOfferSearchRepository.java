package com.gameworld.app.repository.search;

import com.gameworld.app.domain.TradeOffer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TradeOffer entity.
 */
public interface TradeOfferSearchRepository extends ElasticsearchRepository<TradeOffer, Long> {
}
