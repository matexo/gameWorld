(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GamerProfileDeleteController',GamerProfileDeleteController);

    GamerProfileDeleteController.$inject = ['$uibModalInstance', 'entity', 'GamerProfile'];

    function GamerProfileDeleteController($uibModalInstance, entity, GamerProfile) {
        var vm = this;

        vm.gamerProfile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GamerProfile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
