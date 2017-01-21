(function() {
    'use strict';

    angular
        .module('gameWorldApp')
        .controller('MarketOfferController', MarketOfferController);

    MarketOfferController.$inject = ['$scope', '$state', 'MarketOffer', 'MarketOfferSearch', 'ParseLinks', 'AlertService' , 'Conversation','Principal'];

    function MarketOfferController ($scope, $state, MarketOffer, MarketOfferSearch, ParseLinks, AlertService , Conversation, Principal) {
        var vm = this;

        vm.marketOffers = [];
        vm.marketOffersEndByUser = [];
        vm.endedMarketOffers = [];
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

        vm.account = null;
        vm.isAuthenticated = null;

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function loadAll () {
            if(($state.$current.toString() == 'market-offer-my'))
                vm.state = 'my';
            else if(($state.$current.toString() == 'deals'))
                vm.state = 'deals';
            if (vm.currentSearch) {
                MarketOfferSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else if(vm.state == 'my') {
                MarketOffer.getMy({
                    page: vm.page,
                    size: 20,
                    sort: sort()
                }, onSuccess, onError);
            } else if(vm.state == 'deals') {
                vm.marketOffersEndByUser = MarketOffer.getMarketOffersEndByUser({
                    page: vm.page,
                    size: 20
                });
                vm.endedMarketOffers = MarketOffer.getEndedMarketOffers({
                    page: vm.page,
                    size: 20
                });
                console.log(vm.marketOffersEndByUser);
                console.log(vm.endedMarketOffers);
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
                    $state.go('deals');
                });
        }

        vm.sendMessage = function (profileId) {
            console.log(profileId);
            Conversation.getConversationToReceiver({receiverId: profileId} , function (data) {
                vm.conversation = data.id;
                $state.go('conversation-detail.newmessage' ,{id:vm.conversation});
            });
        };

        vm.cancelMarketOffer = function (marketOfferId) {
            MarketOffer.cancelMarketOffer({id: marketOfferId},
                function() {
                    $state.go('deals');
                });
        }
    }
})();
