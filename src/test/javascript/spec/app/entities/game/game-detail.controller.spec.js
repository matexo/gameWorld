'use strict';

describe('Controller Tests', function() {

    describe('Game Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGame, MockGameType, MockPlatform;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGame = jasmine.createSpy('MockGame');
            MockGameType = jasmine.createSpy('MockGameType');
            MockPlatform = jasmine.createSpy('MockPlatform');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Game': MockGame,
                'GameType': MockGameType,
                'Platform': MockPlatform
            };
            createController = function() {
                $injector.get('$controller')("GameDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gameWorldApp:gameUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
