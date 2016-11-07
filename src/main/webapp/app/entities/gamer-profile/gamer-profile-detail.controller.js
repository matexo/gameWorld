(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GamerProfileDetailController', GamerProfileDetailController);

    GamerProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GamerProfile', 'Adress', 'MarketOffer', 'TradeOffer', 'Game', 'Conversation', 'Comment'];

    function GamerProfileDetailController($scope, $rootScope, $stateParams, previousState, entity, GamerProfile, Adress, MarketOffer, TradeOffer, Game, Conversation, Comment) {
        var vm = this;

        vm.gamerProfile = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:gamerProfileUpdate', function(event, result) {
            vm.gamerProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
