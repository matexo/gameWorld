'use strict';

describe('Controller Tests', function() {

    describe('MarketOffer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMarketOffer, MockGame, MockGamerProfile, MockTradeOffer, MockComment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMarketOffer = jasmine.createSpy('MockMarketOffer');
            MockGame = jasmine.createSpy('MockGame');
            MockGamerProfile = jasmine.createSpy('MockGamerProfile');
            MockTradeOffer = jasmine.createSpy('MockTradeOffer');
            MockComment = jasmine.createSpy('MockComment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MarketOffer': MockMarketOffer,
                'Game': MockGame,
                'GamerProfile': MockGamerProfile,
                'TradeOffer': MockTradeOffer,
                'Comment': MockComment
            };
            createController = function() {
                $injector.get('$controller')("MarketOfferDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gameWorldApp:marketOfferUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
