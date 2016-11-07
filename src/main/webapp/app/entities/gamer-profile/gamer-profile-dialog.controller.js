(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GamerProfileDialogController', GamerProfileDialogController);

    GamerProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'GamerProfile', 'Adress', 'MarketOffer', 'TradeOffer', 'Game', 'Conversation', 'Comment'];

    function GamerProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, GamerProfile, Adress, MarketOffer, TradeOffer, Game, Conversation, Comment) {
        var vm = this;

        vm.gamerProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.adresses = Adress.query({filter: 'gamerprofile-is-null'});
        $q.all([vm.gamerProfile.$promise, vm.adresses.$promise]).then(function() {
            if (!vm.gamerProfile.adress || !vm.gamerProfile.adress.id) {
                return $q.reject();
            }
            return Adress.get({id : vm.gamerProfile.adress.id}).$promise;
        }).then(function(adress) {
            vm.adresses.push(adress);
        });
        vm.marketoffers = MarketOffer.query();
        vm.tradeoffers = TradeOffer.query();
        vm.games = Game.query();
        vm.conversations = Conversation.query();
        vm.comments = Comment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gamerProfile.id !== null) {
                GamerProfile.update(vm.gamerProfile, onSaveSuccess, onSaveError);
            } else {
                GamerProfile.save(vm.gamerProfile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:gamerProfileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
