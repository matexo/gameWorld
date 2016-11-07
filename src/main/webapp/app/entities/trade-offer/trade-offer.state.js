(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trade-offer', {
            parent: 'entity',
            url: '/trade-offer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TradeOffers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trade-offer/trade-offers.html',
                    controller: 'TradeOfferController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('trade-offer-detail', {
            parent: 'entity',
            url: '/trade-offer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TradeOffer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trade-offer/trade-offer-detail.html',
                    controller: 'TradeOfferDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TradeOffer', function($stateParams, TradeOffer) {
                    return TradeOffer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trade-offer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trade-offer-detail.edit', {
            parent: 'trade-offer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-offer/trade-offer-dialog.html',
                    controller: 'TradeOfferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TradeOffer', function(TradeOffer) {
                            return TradeOffer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trade-offer.new', {
            parent: 'trade-offer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-offer/trade-offer-dialog.html',
                    controller: 'TradeOfferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                payment: null,
                                timestamp: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trade-offer', null, { reload: 'trade-offer' });
                }, function() {
                    $state.go('trade-offer');
                });
            }]
        })
        .state('trade-offer.edit', {
            parent: 'trade-offer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-offer/trade-offer-dialog.html',
                    controller: 'TradeOfferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TradeOffer', function(TradeOffer) {
                            return TradeOffer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trade-offer', null, { reload: 'trade-offer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trade-offer.delete', {
            parent: 'trade-offer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trade-offer/trade-offer-delete-dialog.html',
                    controller: 'TradeOfferDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TradeOffer', function(TradeOffer) {
                            return TradeOffer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trade-offer', null, { reload: 'trade-offer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
