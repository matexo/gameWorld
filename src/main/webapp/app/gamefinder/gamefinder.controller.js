(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GamefinderController', GamefinderController);

    GamefinderController.$inject = ['$scope', '$state' , 'Gamefinder'];

    function GamefinderController ($scope, $state , Gamefinder) {
        var vm = this;
        vm.gamefinderDTO = {
            "sameCity" : true,
            "perfectMatch" : true,
            "marketOfferId" : 1
        };
        console.log(vm.gamefinderDTO);
        Gamefinder.find(vm.gamefinderDTO);

        vm.changeSameCity = function () {
            if(vm.gamefinderDTO.sameCity == true)
                vm.gamefinderDTO.sameCity = false;
            else vm.gamefinderDTO.sameCity = true;
            Gamefinder.find(vm.gamefinderDTO);
        }


    }
})();
