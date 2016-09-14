var controlCenterApp = angular.module('ccApp', [
        'toaster',
        'ngRoute',
        'ngResource',
        'ngMessages',
        'mgcrea.ngStrap'
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
      	        templateUrl: 'ccView/views/createDepartment.html',
      	  })
      	  .when('/bulkUploadStaff', {
      	        templateUrl: 'ccView/views/bulkUploadStaff.html',
      	  })
        .otherwise({redirectTo: '/home'})
}]);