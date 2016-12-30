(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('ConversationDialogController', ConversationDialogController);

    ConversationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Conversation', 'Message', 'GamerProfile'];

    function ConversationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Conversation, Message, GamerProfile) {
        var vm = this;

        vm.conversation = entity;
        vm.clear = clear;
        vm.save = save;
        vm.lastmessages = Message.query({filter: 'conversation-is-null'});
        $q.all([vm.conversation.$promise, vm.lastmessages.$promise]).then(function() {
            if (!vm.conversation.lastMessage || !vm.conversation.lastMessage.id) {
                return $q.reject();
            }
            return Message.get({id : vm.conversation.lastMessage.id}).$promise;
        }).then(function(lastMessage) {
            vm.lastmessages.push(lastMessage);
        });
        vm.messages = Message.query();
        vm.gamerprofiles = GamerProfile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.conversation.id !== null) {
                Conversation.update(vm.conversation, onSaveSuccess, onSaveError);
            } else {
                Conversation.save(vm.conversation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gameWorldApp:conversationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
