(function () {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('MarketOffer', MarketOffer);

    MarketOffer.$inject = ['$resource', 'DateUtils'];

    function MarketOffer($resource, DateUtils) {
        var resourceUrl = 'api/market-offers/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'},
            'getMarketOffersEndByUser': {method: 'GET', isArray: true, url: "api/market-offers/ended"},
            'getEndedMarketOffers': {method: 'GET', isArray: true, url: "api/market-offers/my/ended"},
            'finalize': {
                method: 'PUT',
                url: "api/market-offers/buy"
            },
            'cancelMarketOffer': {
                method: 'PUT',
                url:'api/market-offers/cancel'
            },
            'getMy': {
                method: 'GET',
                url: 'api/market-offers/my',
                isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            }
        });
    }
})();
