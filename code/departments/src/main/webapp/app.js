var departmentApp = angular.module('depApp', [
        'ngRoute',
        'ngResource',
        'ngMessages',
        'mgcrea.ngStrap'
//        'departmentApp.controlCenterApp'
]);

departmentApp.run(['$rootScope', '$location', 'Authorization', function ($rootScope, $location, Authorization) {

    $rootScope.$on('$routeChangeStart', function (event, next) {
    	Authorization.init()
    	if(!Authorization.isLodIn()){
             $location.path("/logIn");
    	}

    });
}]);

departmentApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider

        .when('/chooserManagment', {
                templateUrl: 'commenViews/chooserManagment.html'
        })
        .when('/logIn', {
            templateUrl: 'commenViews/loginTemplate.html'
        })
        .otherwise({redirectTo: 'chooserManagment'})
}]);