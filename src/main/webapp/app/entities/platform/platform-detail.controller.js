(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('PlatformDetailController', PlatformDetailController);

    PlatformDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Platform'];

    function PlatformDetailController($scope, $rootScope, $stateParams, previousState, entity, Platform) {
        var vm = this;

        vm.platform = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:platformUpdate', function(event, result) {
            vm.platform = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
