(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('gamefinder', {
                parent: 'entity',
                url: '/gamefinder',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/gamefinder/gamefinder.html',
                        controller: 'GamefinderController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                }
            })

    }

})();
