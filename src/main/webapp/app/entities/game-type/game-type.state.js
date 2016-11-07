(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('game-type', {
            parent: 'entity',
            url: '/game-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GameTypes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game-type/game-types.html',
                    controller: 'GameTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('game-type-detail', {
            parent: 'entity',
            url: '/game-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GameType'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/game-type/game-type-detail.html',
                    controller: 'GameTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'GameType', function($stateParams, GameType) {
                    return GameType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'game-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('game-type-detail.edit', {
            parent: 'game-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-type/game-type-dialog.html',
                    controller: 'GameTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GameType', function(GameType) {
                            return GameType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game-type.new', {
            parent: 'game-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-type/game-type-dialog.html',
                    controller: 'GameTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                gameType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('game-type', null, { reload: 'game-type' });
                }, function() {
                    $state.go('game-type');
                });
            }]
        })
        .state('game-type.edit', {
            parent: 'game-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-type/game-type-dialog.html',
                    controller: 'GameTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GameType', function(GameType) {
                            return GameType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game-type', null, { reload: 'game-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('game-type.delete', {
            parent: 'game-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/game-type/game-type-delete-dialog.html',
                    controller: 'GameTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GameType', function(GameType) {
                            return GameType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('game-type', null, { reload: 'game-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
