(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('TradeOfferController', TradeOfferController);

    TradeOfferController.$inject = ['$scope', '$state', 'TradeOffer', 'TradeOfferSearch', 'ParseLinks', 'AlertService'];

    function TradeOfferController ($scope, $state, TradeOffer, TradeOfferSearch, ParseLinks, AlertService) {
        var vm = this;

        vm.tradeOffers = [];
        vm.tradeOffersCreatedByUser = [];
        vm.tradeOffersAssignedToUser = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.search = search;
        loadAll();

        function loadAll () {
            vm.tradeOffersAssignedToUser = TradeOffer.assignedToUser({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            vm.tradeOffersCreatedByUser = TradeOffer.createdByUser({
                page: vm.page,
                size: 20,
                sort: sort()
            }, onSuccess, onError);
            // console.log(vm.tradeOffersAssignedToUser);
            // console.log(vm.tradeOffersCreatedByUser);
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
                    vm.tradeOffers.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.tradeOffers = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear () {
            vm.tradeOffers = [];
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

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.tradeOffers = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }

        vm.acceptTrade = function acceptTradeOffer(tradeOfferId) {
            TradeOffer.acceptTradeOffer({id:tradeOfferId} , loadAll() );
            loadAll();
        };

        vm.rejectTrade = function rejectTrade(tradeOfferId) {
            TradeOffer.rejectTradeOffer({id:tradeOfferId} , loadAll()  );
            loadAll();
        };

        vm.cancelTrade = function cancelTradeOffer(tradeOfferId) {
            TradeOffer.cancelTradeOffer({id:tradeOfferId} , clear() );
        };
    }
})();
