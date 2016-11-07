(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('TradeOfferSearch', TradeOfferSearch);

    TradeOfferSearch.$inject = ['$resource'];

    function TradeOfferSearch($resource) {
        var resourceUrl =  'api/_search/trade-offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
