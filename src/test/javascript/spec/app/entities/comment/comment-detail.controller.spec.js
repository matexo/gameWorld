'use strict';

describe('Controller Tests', function() {

    describe('Comment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockComment, MockGamerProfile, MockMarketOffer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockComment = jasmine.createSpy('MockComment');
            MockGamerProfile = jasmine.createSpy('MockGamerProfile');
            MockMarketOffer = jasmine.createSpy('MockMarketOffer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Comment': MockComment,
                'GamerProfile': MockGamerProfile,
                'MarketOffer': MockMarketOffer
            };
            createController = function() {
                $injector.get('$controller')("CommentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gameWorldApp:commentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
