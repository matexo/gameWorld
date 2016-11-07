(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('GameType', GameType);

    GameType.$inject = ['$resource'];

    function GameType ($resource) {
        var resourceUrl =  'api/game-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
