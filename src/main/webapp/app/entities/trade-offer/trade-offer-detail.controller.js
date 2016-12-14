(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('TradeOfferDetailController', TradeOfferDetailController);

    TradeOfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TradeOffer', 'Game', 'GamerProfile', 'MarketOffer'];

    function TradeOfferDetailController($scope, $rootScope, $stateParams, previousState, entity, TradeOffer, Game, GamerProfile, MarketOffer) {
        var vm = this;

        vm.tradeOffer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:tradeOfferUpdate', function(event, result) {
            vm.tradeOffer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
