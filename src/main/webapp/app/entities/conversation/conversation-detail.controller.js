(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('ConversationDetailController', ConversationDetailController);

    ConversationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Conversation', 'Message', 'GamerProfile'];

    function ConversationDetailController($scope, $rootScope, $stateParams, previousState, entity, Conversation, Message, GamerProfile) {
        var vm = this;

        vm.conversation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:conversationUpdate', function(event, result) {
            vm.conversation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
