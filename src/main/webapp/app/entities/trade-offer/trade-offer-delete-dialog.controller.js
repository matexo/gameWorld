(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('TradeOfferDeleteController',TradeOfferDeleteController);

    TradeOfferDeleteController.$inject = ['$uibModalInstance', 'entity', 'TradeOffer'];

    function TradeOfferDeleteController($uibModalInstance, entity, TradeOffer) {
        var vm = this;

        vm.tradeOffer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TradeOffer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
