(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GameTypeDeleteController',GameTypeDeleteController);

    GameTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'GameType'];

    function GameTypeDeleteController($uibModalInstance, entity, GameType) {
        var vm = this;

        vm.gameType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GameType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
