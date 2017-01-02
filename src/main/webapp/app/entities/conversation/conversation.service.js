(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('Conversation', Conversation);

    Conversation.$inject = ['$resource', 'DateUtils'];

    function Conversation ($resource, DateUtils) {
        var resourceUrl =  'api/conversations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                isArray:false,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.lastUpdate = DateUtils.convertDateTimeFromServer(data.lastUpdate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getConversationToReceiver': { method:'GET' , url:"api/conversations/receiver/:receiverId" ,isArray:false},
            'getAllMessagesToConversation': { method: 'GET' , isArray:true , url:"api/messages/conversation/:conversationId" }
        });
    }
})();
