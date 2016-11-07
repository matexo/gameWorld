'use strict';

describe('Controller Tests', function() {

    describe('GamerProfile Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGamerProfile, MockAdress, MockMarketOffer, MockTradeOffer, MockGame, MockConversation, MockComment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGamerProfile = jasmine.createSpy('MockGamerProfile');
            MockAdress = jasmine.createSpy('MockAdress');
            MockMarketOffer = jasmine.createSpy('MockMarketOffer');
            MockTradeOffer = jasmine.createSpy('MockTradeOffer');
            MockGame = jasmine.createSpy('MockGame');
            MockConversation = jasmine.createSpy('MockConversation');
            MockComment = jasmine.createSpy('MockComment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GamerProfile': MockGamerProfile,
                'Adress': MockAdress,
                'MarketOffer': MockMarketOffer,
                'TradeOffer': MockTradeOffer,
                'Game': MockGame,
                'Conversation': MockConversation,
                'Comment': MockComment
            };
            createController = function() {
                $injector.get('$controller')("GamerProfileDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gameWorldApp:gamerProfileUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
