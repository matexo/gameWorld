(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MarketOfferDetailController', MarketOfferDetailController);

    MarketOfferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MarketOffer', 'Game', 'GamerProfile', 'TradeOffer', 'Comment' , 'Conversation' , '$state'];

    function MarketOfferDetailController($scope, $rootScope, $stateParams, previousState, entity, MarketOffer, Game, GamerProfile, TradeOffer, Comment , Conversation , $state) {
        var vm = this;

        vm.marketOffer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:marketOfferUpdate', function(event, result) {
            vm.marketOffer = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.sendMessage = function (profileId) {
            console.log(profileId);
            Conversation.getConversationToReceiver({receiverId: profileId} , function (data) {
                vm.conversation = data.id;
                $state.go('conversation-detail.newmessage' ,{id:vm.conversation});
            });
        };

        function finalizeOffer(id) {
            MarketOffer.finalize({id: id} , function() {
                $state.go('market-offer-my')
            }
            );
        }
    }
})();
