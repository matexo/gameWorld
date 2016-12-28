(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('Adress', Adress);

    Adress.$inject = ['$resource'];

    function Adress ($resource) {
        var resourceUrl =  'api/adresses/:id';

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
            'getUserAdress': { method: 'GET' , url:"api/adresses/my", isArray:false},
            'update': { method:'PUT' }
        });
    }
})();
