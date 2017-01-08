(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('Gamefinder', Gamefinder);

    Gamefinder.$inject = ['$resource', 'DateUtils'];

    function Gamefinder ($resource, DateUtils) {
        var resourceUrl =  'api/gamefinder';

        return $resource(resourceUrl, {}, {
            'find': { method: 'GET', isArray: true}
        });
    }
})();
