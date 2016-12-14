(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MessageDetailController', MessageDetailController);

    MessageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Message', 'GamerProfile', 'Conversation'];

    function MessageDetailController($scope, $rootScope, $stateParams, previousState, entity, Message, GamerProfile, Conversation) {
        var vm = this;

        vm.message = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:messageUpdate', function(event, result) {
            vm.message = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
