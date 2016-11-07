(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GameDetailController', GameDetailController);

    GameDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Game', 'GameType', 'Platform'];

    function GameDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Game, GameType, Platform) {
        var vm = this;

        vm.game = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gameWorldApp:gameUpdate', function(event, result) {
            vm.game = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
