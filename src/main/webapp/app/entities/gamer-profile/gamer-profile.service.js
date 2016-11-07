(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('GamerProfile', GamerProfile);

    GamerProfile.$inject = ['$resource'];

    function GamerProfile ($resource) {
        var resourceUrl =  'api/gamer-profiles/:id';

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
