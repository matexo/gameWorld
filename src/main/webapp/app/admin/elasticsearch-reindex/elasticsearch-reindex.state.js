(function () {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('elasticsearch-reindex', {
            parent: 'admin',
            url: '/elasticsearch-reindex',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'elasticsearch.reindex.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/admin/elasticsearch-reindex//elasticsearch-reindex.html',
                    controller: 'ElasticsearchReindexController',
                    controllerAs: 'vm'
                }
            }
        }).state('elasticsearch-reindex.dialog', {
            parent: 'elasticsearch-reindex',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function ($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/admin/elasticsearch-reindex//elasticsearch-reindex-dialog.html',
                    controller: 'ElasticsearchReindexDialogController',
                    controllerAs: 'vm',
                    size: 'sm'
                }).result.finally(function () {
                    $state.go('^');
                });
            }]
        });
    }
})();
