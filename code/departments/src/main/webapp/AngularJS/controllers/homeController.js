'use strict';
/**
 * created by David Sajdl
 */
angular.module('testApp', [])
.controller('MainCtrl',[ '$http', function($http) {
	var self = this;
	self.message = 'Hello world ';
		
	self.init = function(){
		var Url = '/department/rest/rest/getData';
		return $http(
				{
					method: 'GET',
					url: Url
				})
				.then(function successCallback(response)
				{
					console.log("response: ", response);
					var restData = response.data.split(',');
					self.restCall =restData;
					console.log("self.restCall", self.restCall);
				},
				function errorCallBack(error){
					self.restCall = error;
				});
	}
}
]);