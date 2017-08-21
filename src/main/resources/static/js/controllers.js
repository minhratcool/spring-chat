'use strict';

/* Controllers */

angular.module('springChat.controllers', ['toaster', 'zInfiniteScroll'])
    .controller('ChatController', ['$scope', '$http', '$location', '$interval', 'toaster', 'ChatSocket', function ($scope, $http, $location, $interval, toaster, chatSocket) {

        var typing = undefined;

        $scope.username = '';
        $scope.sendTo = '';
        $scope.participants = [];
        $scope.newMessage = '';
        $scope.onOffer = 1;
        $scope.userId = '';
        $scope.offerMessages = [];
        $scope.isLoading = false;
        $scope.onPage = 0;


        $scope.sendMessageOnOffer = function () {
            var destination = "/app/chat." + $scope.username + ".on." + $scope.onOffer;
            // $scope.offerMessages.unshift({message: $scope.newMessage, username: 'you', priv: true, to: $scope.sendTo});
            chatSocket.send(destination, {}, JSON.stringify({message: $scope.newMessage}));
            $scope.newMessage = '';
        };

        $scope.startTyping = function () {
            // Don't send notification if we are still typing or we are typing a private message
            if (angular.isDefined(typing) || $scope.sendTo != "everyone") return;

            typing = $interval(function () {
                $scope.stopTyping();
            }, 500);

            chatSocket.send("/topic/chat.typing", {}, JSON.stringify({username: $scope.username, typing: true}));
        };

        $scope.stopTyping = function () {
            if (angular.isDefined(typing)) {
                $interval.cancel(typing);
                typing = undefined;

                chatSocket.send("/topic/chat.typing", {}, JSON.stringify({username: $scope.username, typing: false}));
            }
        };


        $scope.loadMore = function () {
            if ($scope.isLoading) return;
            $scope.isLoading = true;

            var url = "http://localhost:8888/api/offer/" + $scope.onOffer + "/messages?page=" + $scope.onPage + "&pageSize=20";
            $http.get(url).success(function(data, status) {
                var items = data;
                for (var i = 0; i < items.length; i++) {
                    $scope.offerMessages.push({message: items[i].text, username: items[i].username});
                }
                $scope.onPage ++;
                $scope.isLoading = false;
            }.bind($scope));
        };

        var initStompClient = function () {
            $scope.loadMore();

            chatSocket.init('/ws');

            chatSocket.connect(function (frame) {

                $scope.username = frame.headers['user-name'];


                chatSocket.subscribe("/topic/chat.typing", function (message) {
                    var parsed = JSON.parse(message.body);
                    if (parsed.username == $scope.username) return;

                    for (var index in $scope.participants) {
                        var participant = $scope.participants[index];

                        if (participant.username == parsed.username) {
                            $scope.participants[index].typing = parsed.typing;
                        }
                    }
                });

                chatSocket.subscribe("/topic/offer/" + $scope.onOffer + "/messages", function (message) {
                    var parsed = JSON.parse(message.body);
                    $scope.offerMessages.unshift(parsed);
                });

                chatSocket.subscribe("/user/exchange/amq.direct/errors", function (message) {
                    toaster.pop('error', "Error", message.body);
                });

            }, function (error) {
                toaster.pop('error', 'Error', 'Connection error ' + error);

            });
        };

        initStompClient();
    }]);
	