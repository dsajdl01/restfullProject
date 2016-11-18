var departmentApp = angular.module('depApp', [
        'ngRoute',
        'ngResource',
        'ngMessages',
        'ngStorage',
        'mgcrea.ngStrap'
]);

departmentApp.run(['$rootScope', '$location', 'Authorization', function ($rootScope, $location, Authorization) {

    $rootScope.$on('$routeChangeStart', function (event, next) {
    	if(!Authorization.isUserLogin()){
             $location.path("/logIn");
    	}
    });
}]);

departmentApp.config(['$routeProvider', function($routeProvider) {
        $routeProvider

        .when('/chooserManagment', {
                templateUrl: 'commenViews/chooserManagment.html',

        })
        .when('/logIn', {
            templateUrl: 'commenViews/loginTemplate.html'
        })
        .otherwise({redirectTo: 'chooserManagment'})
}]);