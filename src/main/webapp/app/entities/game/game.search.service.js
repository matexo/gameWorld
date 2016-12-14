(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('GameSearch', GameSearch);

    GameSearch.$inject = ['$resource'];

    function GameSearch($resource) {
        var resourceUrl =  'api/_search/games/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
