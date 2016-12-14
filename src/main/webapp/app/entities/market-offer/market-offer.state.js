(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('market-offer', {
            parent: 'entity',
            url: '/market-offer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MarketOffers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/market-offer/market-offers.html',
                    controller: 'MarketOfferController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('market-offer-detail', {
            parent: 'entity',
            url: '/market-offer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MarketOffer'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/market-offer/market-offer-detail.html',
                    controller: 'MarketOfferDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MarketOffer', function($stateParams, MarketOffer) {
                    return MarketOffer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'market-offer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('market-offer-detail.edit', {
            parent: 'market-offer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/market-offer/market-offer-dialog.html',
                    controller: 'MarketOfferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MarketOffer', function(MarketOffer) {
                            return MarketOffer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('market-offer.new', {
            parent: 'market-offer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/market-offer/market-offer-dialog.html',
                    controller: 'MarketOfferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                createDate: null,
                                endDate: null,
                                offerType: null,
                                price: null,
                                offerStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('market-offer', null, { reload: 'market-offer' });
                }, function() {
                    $state.go('market-offer');
                });
            }]
        })
        .state('market-offer.edit', {
            parent: 'market-offer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/market-offer/market-offer-dialog.html',
                    controller: 'MarketOfferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MarketOffer', function(MarketOffer) {
                            return MarketOffer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('market-offer', null, { reload: 'market-offer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('market-offer.delete', {
            parent: 'market-offer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/market-offer/market-offer-delete-dialog.html',
                    controller: 'MarketOfferDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MarketOffer', function(MarketOffer) {
                            return MarketOffer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('market-offer', null, { reload: 'market-offer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
