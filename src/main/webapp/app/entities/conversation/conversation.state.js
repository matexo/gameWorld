(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('conversation', {
            parent: 'entity',
            url: '/conversation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Conversations'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conversation/conversations.html',
                    controller: 'ConversationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('conversation-detail', {
            parent: 'entity',
            url: '/conversation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Conversation'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/conversation/conversation-detail.html',
                    controller: 'ConversationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Conversation', function($stateParams, Conversation) {
                    return Conversation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'conversation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('conversation-detail.edit', {
            parent: 'conversation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-dialog.html',
                    controller: 'ConversationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conversation', function(Conversation) {
                            return Conversation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conversation.new', {
            parent: 'conversation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-dialog.html',
                    controller: 'ConversationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                hasNew: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('conversation', null, { reload: 'conversation' });
                }, function() {
                    $state.go('conversation');
                });
            }]
        })
        .state('conversation.edit', {
            parent: 'conversation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-dialog.html',
                    controller: 'ConversationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Conversation', function(Conversation) {
                            return Conversation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conversation', null, { reload: 'conversation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('conversation.delete', {
            parent: 'conversation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/conversation/conversation-delete-dialog.html',
                    controller: 'ConversationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Conversation', function(Conversation) {
                            return Conversation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('conversation', null, { reload: 'conversation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
