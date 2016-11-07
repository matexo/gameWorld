'use strict';

describe('Controller Tests', function() {

    describe('TradeOffer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTradeOffer, MockGame, MockGamerProfile, MockMarketOffer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTradeOffer = jasmine.createSpy('MockTradeOffer');
            MockGame = jasmine.createSpy('MockGame');
            MockGamerProfile = jasmine.createSpy('MockGamerProfile');
            MockMarketOffer = jasmine.createSpy('MockMarketOffer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'TradeOffer': MockTradeOffer,
                'Game': MockGame,
                'GamerProfile': MockGamerProfile,
                'MarketOffer': MockMarketOffer
            };
            createController = function() {
                $injector.get('$controller')("TradeOfferDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gameWorldApp:tradeOfferUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
