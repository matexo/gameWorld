(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MarketOfferDetailController', MarketOfferDetailController);

    MarketOfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MarketOffer', 'Game', 'GamerProfile', 'TradeOffer', 'Comment'];

    function MarketOfferDetailController($scope, $rootScope, $stateParams, previousState, entity, MarketOffer, Game, GamerProfile, TradeOffer, Comment) {
        var vm = this;

        vm.marketOffer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:marketOfferUpdate', function(event, result) {
            vm.marketOffer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
