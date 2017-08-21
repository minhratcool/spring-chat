/* Directives */

angular.module('springChat.directives', [])
	.directive('printMessage', function () {
	    return {
	    	restrict: 'A',
	        template: '<strong>{{message.username}}:</strong> {{message.message}}<br/>'
	       
	    };
});
