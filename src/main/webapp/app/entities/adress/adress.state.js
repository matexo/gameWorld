(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('adress', {
            parent: 'entity',
            url: '/adress',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Adresses'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adress/adresses.html',
                    controller: 'AdressController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('adress-detail', {
            parent: 'entity',
            url: '/adress/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Adress'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adress/adress-detail.html',
                    controller: 'AdressDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Adress', function($stateParams, Adress) {
                    return Adress.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'adress',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('adress-detail.edit', {
            parent: 'adress-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adress/adress-dialog.html',
                    controller: 'AdressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Adress', function(Adress) {
                            return Adress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adress.new', {
            parent: 'adress',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adress/adress-dialog.html',
                    controller: 'AdressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                streetName: null,
                                houseNo: null,
                                city: null,
                                zipCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('adress', null, { reload: 'adress' });
                }, function() {
                    $state.go('adress');
                });
            }]
        })
        .state('adress.edit', {
            parent: 'adress',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adress/adress-dialog.html',
                    controller: 'AdressDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Adress', function(Adress) {
                            return Adress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('adress', null, { reload: 'adress' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adress.delete', {
            parent: 'adress',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adress/adress-delete-dialog.html',
                    controller: 'AdressDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Adress', function(Adress) {
                            return Adress.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('adress', null, { reload: 'adress' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
