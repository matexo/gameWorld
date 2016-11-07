(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('MarketOfferSearch', MarketOfferSearch);

    MarketOfferSearch.$inject = ['$resource'];

    function MarketOfferSearch($resource) {
        var resourceUrl =  'api/_search/market-offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
