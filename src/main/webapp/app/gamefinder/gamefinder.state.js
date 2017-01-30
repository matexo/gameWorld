(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('gamefinder', {
                parent: 'app',
                url: '/gamefinder/:id',
                params: { id: null},
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/gamefinder/gamefinder.html',
                        controller: 'GamefinderController',
                        controllerAs: 'vm'
                    }
                }
            })
    }
})();
