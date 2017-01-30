(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GamefinderController', GamefinderController);

    GamefinderController.$inject = ['$scope', '$state' ,'$stateParams' , 'Gamefinder' , 'MarketOffer' , 'AlertService' , 'Conversation'];

    function GamefinderController ($scope, $state ,$stateParams, Gamefinder , MarketOffer , AlertService , Conversation) {
        var vm = this;
        vm.matchedOffers = [];
        vm.baseMarketOffer = MarketOffer.get({id:$stateParams.id});
        vm.gamefinderDTO = {
            "sameCity" : true,
            "perfectMatch" : true,
            "marketOfferId" : $stateParams.id
        };
        console.log(vm.gamefinderDTO);
        console.log(vm.baseMarketOffer);
        Gamefinder.find(vm.gamefinderDTO);

        vm.changeSameCity = function () {
            vm.matchedOffers = [];
            vm.gamefinderDTO.sameCity = !vm.gamefinderDTO.sameCity;
            Gamefinder.find(vm.gamefinderDTO , onSuccess , onError);

            function onSuccess(data) {
                for (var i = 0; i < data.length; i++) {
                    vm.matchedOffers.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };

        vm.changePerfectMatching = function () {
            vm.matchedOffers = [];
            vm.gamefinderDTO.perfectMatch = !vm.gamefinderDTO.perfectMatch;
            Gamefinder.find(vm.gamefinderDTO , onSuccess , onError);

            function onSuccess(data) {
                for (var i = 0; i < data.length; i++) {
                    vm.matchedOffers.push(data[i]);
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        vm.sendMessage = function (profileId) {
            console.log(profileId);
            Conversation.getConversationToReceiver({receiverId: profileId} , function (data) {
                vm.conversation = data.id;
                $state.go('conversation-detail.newmessage' ,{id:vm.conversation});
            });
        };


    }
})();
