(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('TradeOffer', TradeOffer);

    TradeOffer.$inject = ['$resource', 'DateUtils'];

    function TradeOffer ($resource, DateUtils) {
        var resourceUrl =  'api/trade-offers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'createdByUser': {method: 'GET' , isArray: true , url:'api/trade-offers/created'},
            'assignedToUser': {method: 'GET' , isArray: true , url:'api/trade-offers/assigned'},
            'acceptTradeOffer': {method: 'PUT' , url:'api/trade-offers/accept/:id'},
            'rejectTradeOffer': {method: 'PUT' , url:'api/trade-offers/reject/:id'},
            'cancelTradeOffer': {method: 'PUT' , url:'api/trade-offers/cancel/:id'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.timestamp = DateUtils.convertDateTimeFromServer(data.timestamp);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
