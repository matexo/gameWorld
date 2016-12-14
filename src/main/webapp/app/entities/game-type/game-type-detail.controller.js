(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GameTypeDetailController', GameTypeDetailController);

    GameTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GameType'];

    function GameTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, GameType) {
        var vm = this;

        vm.gameType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:gameTypeUpdate', function(event, result) {
            vm.gameType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
