(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('PlatformSearch', PlatformSearch);

    PlatformSearch.$inject = ['$resource'];

    function PlatformSearch($resource) {
        var resourceUrl =  'api/_search/platforms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
