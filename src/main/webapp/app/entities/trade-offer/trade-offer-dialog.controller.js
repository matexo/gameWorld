(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('TradeOfferDialogController', TradeOfferDialogController);

    TradeOfferDialogController.$inject = [ '$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TradeOffer', 'Game', 'GamerProfile', 'MarketOffer'];

    function TradeOfferDialogController ( $timeout, $scope, $stateParams, $uibModalInstance, entity, TradeOffer, Game, GamerProfile, MarketOffer) {
        var vm = this;

        vm.tradeOffer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.games = Game.query();
        vm.gamerprofiles = GamerProfile.query();
        vm.marketOfferId = $stateParams.marketOfferId;
        vm.marketoffers = [];
        vm.tradeOffer.marketOffer = MarketOffer.get({id : vm.marketOfferId});

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.tradeOffer.id !== null) {
                TradeOffer.update(vm.tradeOffer, onSaveSuccess, onSaveError);
            } else {
                TradeOffer.save(vm.tradeOffer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:tradeOfferUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.timestamp = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
