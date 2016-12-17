(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MarketOfferController', MarketOfferController);

    MarketOfferController.$inject = ['$scope', '$state', 'MarketOffer', 'MarketOfferSearch', 'ParseLinks', 'AlertService'];

    function MarketOfferController ($scope, $state, MarketOffer, MarketOfferSearch, ParseLinks, AlertService) {
        var vm = this;

        vm.marketOffers = [];
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
        vm.finalizeOffer = finalizeOffer;

        loadAll();

        function loadAll () {
            if(($state.$current.toString() == 'market-offer-my'))
                vm.my = 1;
            if (vm.currentSearch) {
                MarketOfferSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else if(vm.my == 1) {
                MarketOffer.getMy({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                MarketOffer.query({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            }
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
                    vm.marketOffers.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.marketOffers = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear () {
            vm.marketOffers = [];
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
            vm.marketOffers = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }

        function finalizeOffer(id) {
            MarketOffer.finalize({id: id},
                function() {
                    $state.go('market-offer-my');
                });
        }
    }
})();
