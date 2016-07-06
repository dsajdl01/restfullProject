var controlCenterApp = angular.module('ccApp', [
        'ngRoute'
        ]);

controlCenterApp.config(['$routeProvider', function($routeProvider) {
	  $routeProvider

      // Add more views here for routing
      .when('/home', {
      	        templateUrl: 'ccView/views/homeview.html',
      	  })
	  .when('/report', {
	        templateUrl: 'ccView/views/reportview.html',
	  })
	  .when('/createdepartment', {
      	        templateUrl: 'ccView/views/createdepartment.html',
      	  })
        .otherwise({redirectTo: '/home'})
}]);