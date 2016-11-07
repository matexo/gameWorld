(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('GameDialogController', GameDialogController);

    GameDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Game', 'GameType', 'Platform'];

    function GameDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Game, GameType, Platform) {
        var vm = this;

        vm.game = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.gametypes = GameType.query({filter: 'game-is-null'});
        $q.all([vm.game.$promise, vm.gametypes.$promise]).then(function() {
            if (!vm.game.gameType || !vm.game.gameType.id) {
                return $q.reject();
            }
            return GameType.get({id : vm.game.gameType.id}).$promise;
        }).then(function(gameType) {
            vm.gametypes.push(gameType);
        });
        vm.platforms = Platform.query({filter: 'game-is-null'});
        $q.all([vm.game.$promise, vm.platforms.$promise]).then(function() {
            if (!vm.game.platform || !vm.game.platform.id) {
                return $q.reject();
            }
            return Platform.get({id : vm.game.platform.id}).$promise;
        }).then(function(platform) {
            vm.platforms.push(platform);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.game.id !== null) {
                Game.update(vm.game, onSaveSuccess, onSaveError);
            } else {
                Game.save(vm.game, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:gameUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setCoverImage = function ($file, game) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        game.coverImage = base64Data;
                        game.coverImageContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.timestamp = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
