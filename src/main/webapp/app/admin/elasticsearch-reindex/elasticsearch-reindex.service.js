(function () {
    'use strict';

    angular
        .module('gameWorldApp')
        .factory('ElasticsearchReindex', ElasticsearchReindex);

    ElasticsearchReindex.$inject = ['$resource'];

    function ElasticsearchReindex($resource) {
        var service = $resource('api/elasticsearch/index', {}, {
            'reindex': {method: 'POST'}
        });

        return service;
    }
})();
