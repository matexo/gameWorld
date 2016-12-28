(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('Game', Game);

    Game.$inject = ['$resource', 'DateUtils'];

    function Game ($resource, DateUtils) {
        var resourceUrl =  'api/games/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
            'addToWishList': {method:'PUT' , url: 'api/games/:id'},
            'getFromWishlist': {method: 'GET' , isArray: true , url:"api/games/wishlist"},
            'removeGameFromWishlist': {method: 'DELETE' , url:"api/games/wishlist/:id"},
            'update': { method:'PUT' }
        });
    }
})();
