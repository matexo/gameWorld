(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MarketOfferDeleteController',MarketOfferDeleteController);

    MarketOfferDeleteController.$inject = ['$uibModalInstance', 'entity', 'MarketOffer'];

    function MarketOfferDeleteController($uibModalInstance, entity, MarketOffer) {
        var vm = this;

        vm.marketOffer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MarketOffer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
