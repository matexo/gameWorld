(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('AdressDialogController', AdressDialogController);

    AdressDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Adress', 'GamerProfile'];

    function AdressDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Adress, GamerProfile) {
        var vm = this;

        vm.adress = entity;
        vm.clear = clear;
        vm.save = save;
        vm.gamerprofiles = GamerProfile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.adress.id !== null) {
                Adress.update(vm.adress, onSaveSuccess, onSaveError);
            } else {
                Adress.save(vm.adress, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:adressUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
