(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('CommentDetailController', CommentDetailController);

    CommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Comment', 'GamerProfile', 'MarketOffer'];

    function CommentDetailController($scope, $rootScope, $stateParams, previousState, entity, Comment, GamerProfile, MarketOffer) {
        var vm = this;

        vm.comment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:commentUpdate', function(event, result) {
            vm.comment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
