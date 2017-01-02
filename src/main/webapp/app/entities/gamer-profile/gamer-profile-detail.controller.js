(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GamerProfileDetailController', GamerProfileDetailController);

    GamerProfileDetailController.$inject = ['$scope', '$state' ,'$rootScope', '$stateParams', 'previousState', 'entity', 'GamerProfile', 'Adress', 'MarketOffer', 'TradeOffer', 'Game', 'Conversation', 'Comment'];

    function GamerProfileDetailController($scope, $state ,$rootScope, $stateParams, previousState, entity, GamerProfile, Adress, MarketOffer, TradeOffer, Game, Conversation, Comment) {
        var vm = this;

        vm.gamerProfile = entity;
        vm.previousState = previousState.name;
        vm.conversation = null;

        var unsubscribe = $rootScope.$on('gameWorldApp:gamerProfileUpdate', function(event, result) {
            vm.gamerProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.sendMessage = function (profileId) {
            Conversation.getConversationToReceiver({receiverId: profileId} , function (data) {
                vm.conversation = data.id;
                $state.go('conversation-detail.newmessage' ,{id:vm.conversation});
            });
            // console.log(vm.conversation);

        }
    }
})();
