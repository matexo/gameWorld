(function () {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('ConversationDetailController', ConversationDetailController);

    ConversationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ParseLinks', 'Conversation', 'Message', 'GamerProfile','AlertService' , '$state'];

    function ConversationDetailController($scope, $rootScope, $stateParams, previousState, entity, ParseLinks, Conversation, Message, GamerProfile,AlertService , $state) {
        var vm = this;
        console.log($stateParams);

        vm.conversation = entity;
        vm.previousState = previousState.name;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.messages = [];
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        vm.loadPage = loadPage;
        vm.loadAll = loadAll;
        loadAll();

        function loadAll() {
            console.log(vm.conversation);
            Conversation.getAllMessagesToConversation({
                page: vm.page,
                size: 20,
                conversationId: $stateParams.id,
                sort: sort()
            }, onSuccess , onError);

            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.messages.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
            console.log(vm.messages);
        }

        function reset() {
            vm.page = 0;
            vm.messages = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear() {
            vm.messages = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        }


        var unsubscribe = $rootScope.$on('gameWorldApp:conversationUpdate', function (event, result) {
            vm.conversation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
