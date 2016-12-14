(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('GamerProfileSearch', GamerProfileSearch);

    GamerProfileSearch.$inject = ['$resource'];

    function GamerProfileSearch($resource) {
        var resourceUrl =  'api/_search/gamer-profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
