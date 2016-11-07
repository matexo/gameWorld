(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GameTypeDialogController', GameTypeDialogController);

    GameTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GameType'];

    function GameTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GameType) {
        var vm = this;

        vm.gameType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gameType.id !== null) {
                GameType.update(vm.gameType, onSaveSuccess, onSaveError);
            } else {
                GameType.save(vm.gameType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:gameTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
