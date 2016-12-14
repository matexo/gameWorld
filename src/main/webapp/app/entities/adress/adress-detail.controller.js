(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('AdressDetailController', AdressDetailController);

    AdressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Adress', 'GamerProfile'];

    function AdressDetailController($scope, $rootScope, $stateParams, previousState, entity, Adress, GamerProfile) {
        var vm = this;

        vm.adress = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gameWorldApp:adressUpdate', function(event, result) {
            vm.adress = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
