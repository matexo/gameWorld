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

        var unsubscribe = $rootScope.$on('gameWorldApp:gamerProfileUpdate', function(event, result) {
            vm.gamerProfile = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.sendMessage = function (profileId) {
            vm.conversation = Conversation.getConversationToReceiver({receiverId: profileId});
            console.log(vm.conversation);
            $state.go('conversation-detail' ,{id:5});
        }
    }
})();
