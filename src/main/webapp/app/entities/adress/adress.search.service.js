(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('AdressSearch', AdressSearch);

    AdressSearch.$inject = ['$resource'];

    function AdressSearch($resource) {
        var resourceUrl =  'api/_search/adresses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
