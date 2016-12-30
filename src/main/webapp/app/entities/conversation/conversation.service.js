(function() {
    'use strict';
    angular
        .module('gameWorldApp')
        .factory('Conversation', Conversation);

    Conversation.$inject = ['$resource'];

    function Conversation ($resource) {
        var resourceUrl =  'api/conversations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
            'getConversationToReceiver': { method:'GET' , url:"api/conversations/receiver/:receiverId"}
        });
    }
})();
