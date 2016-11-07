(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('GameTypeSearch', GameTypeSearch);

    GameTypeSearch.$inject = ['$resource'];

    function GameTypeSearch($resource) {
        var resourceUrl =  'api/_search/game-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
