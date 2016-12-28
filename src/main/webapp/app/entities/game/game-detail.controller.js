(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GameDetailController', GameDetailController);

    GameDetailController.$inject = ['$scope', '$state' , '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Game', 'GameType', 'Platform'];

    function GameDetailController($scope,$state , $rootScope, $stateParams, previousState, DataUtils, entity, Game, GameType, Platform) {
        var vm = this;

        vm.game = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gameWorldApp:gameUpdate', function(event, result) {
            vm.game = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.addToWishList = function addToWishList(gameId) {
            Game.addToWishList({id:gameId} , $state.go('wishlist'));
        };
    }
})();
