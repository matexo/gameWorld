(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('gamer-profile', {
            parent: 'entity',
            url: '/gamer-profile',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GamerProfiles'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gamer-profile/gamer-profiles.html',
                    controller: 'GamerProfileController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('gamer-profile-detail', {
            parent: 'entity',
            url: '/gamer-profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'GamerProfile'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/gamer-profile/gamer-profile-detail.html',
                    controller: 'GamerProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'GamerProfile', function($stateParams, GamerProfile) {
                    return GamerProfile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'gamer-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('gamer-profile-detail.edit', {
            parent: 'gamer-profile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamer-profile/gamer-profile-dialog.html',
                    controller: 'GamerProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GamerProfile', function(GamerProfile) {
                            return GamerProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gamer-profile.new', {
            parent: 'gamer-profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamer-profile/gamer-profile-dialog.html',
                    controller: 'GamerProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                surname: null,
                                phone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('gamer-profile', null, { reload: 'gamer-profile' });
                }, function() {
                    $state.go('gamer-profile');
                });
            }]
        })
        .state('gamer-profile.edit', {
            parent: 'gamer-profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamer-profile/gamer-profile-dialog.html',
                    controller: 'GamerProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GamerProfile', function(GamerProfile) {
                            return GamerProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gamer-profile', null, { reload: 'gamer-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('gamer-profile.delete', {
            parent: 'gamer-profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/gamer-profile/gamer-profile-delete-dialog.html',
                    controller: 'GamerProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GamerProfile', function(GamerProfile) {
                            return GamerProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('gamer-profile', null, { reload: 'gamer-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
