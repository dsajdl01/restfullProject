var controlCenterApp = angular.module('ccApp', [
        'toaster',
        'ngRoute',
        'ngResource',
        'ngMessages',
        'ngStorage',
        'ui.validate',
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
	  .when('/addSingleStaff',{
            templateUrl: 'ccView/views/addSingleStaff.html',
	  })
	  .when('/modifySingleStaff',{
            templateUrl: 'ccView/views/modifySingleStaff.html',
      })
	  .when('/createdepartment', {
      	        templateUrl: 'ccView/views/createDepartment.html',
      	  })
      	  .when('/bulkUploadStaff', {
      	        templateUrl: 'ccView/views/bulkUploadStaff.html',
      	  })
        .otherwise({redirectTo: '/home'})
}]);


var UTILS = {
    responseErrorHandler: function(message , responseError) {
        if ( responseError.data && responseError.data.message ) {
            return message + " Error message: " + responseError.data.message;
        }
        return message + " Status error: " + responseError.status
    }
}