(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MarketOfferDialogController', MarketOfferDialogController);

    MarketOfferDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MarketOffer', 'Game', 'GamerProfile', 'TradeOffer', 'Comment'];

    function MarketOfferDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MarketOffer, Game, GamerProfile, TradeOffer, Comment) {
        var vm = this;

        vm.marketOffer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.games = Game.query({
            page: 0,
            size: 100,
            filter: 'marketoffer-is-null'});
        $q.all([vm.marketOffer.$promise, vm.games.$promise]).then(function() {
            if (!vm.marketOffer.game || !vm.marketOffer.game.id) {
                return $q.reject();
            }
            return Game.get({id : vm.marketOffer.game.id}).$promise;
        }).then(function(game) {
            vm.games.push(game);
        });
        vm.endofferprofiles = GamerProfile.query({filter: 'marketoffer-is-null'});
        $q.all([vm.marketOffer.$promise, vm.endofferprofiles.$promise]).then(function() {
            if (!vm.marketOffer.endOfferProfile || !vm.marketOffer.endOfferProfile.id) {
                return $q.reject();
            }
            return GamerProfile.get({id : vm.marketOffer.endOfferProfile.id}).$promise;
        }).then(function(endOfferProfile) {
            vm.endofferprofiles.push(endOfferProfile);
        });
        vm.tradeoffers = TradeOffer.query();
        vm.comments = Comment.query();
        vm.gamerprofiles = GamerProfile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.marketOffer.id !== null) {
                MarketOffer.update(vm.marketOffer, onSaveSuccess, onSaveError);
            } else {
                MarketOffer.save(vm.marketOffer, onSaveSuccess, onSaveError);
            }
        }

        function finalizeOffer(id) {
            MarketOffer.finalize({id: id},
                function() {
                    $state.go('market-offer-my');
                });
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:marketOfferUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
