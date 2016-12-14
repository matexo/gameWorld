(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('AdressDeleteController',AdressDeleteController);

    AdressDeleteController.$inject = ['$uibModalInstance', 'entity', 'Adress'];

    function AdressDeleteController($uibModalInstance, entity, Adress) {
        var vm = this;

        vm.adress = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Adress.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
