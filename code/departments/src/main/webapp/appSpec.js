describe('Module: depApp', function() {

   	beforeEach(module('depApp'));

   	var $rootScope, locationMock, authMock;

    beforeEach(module(function($provide) {
            locationMock = {
    			path: function(path) {}
            };

            authMock = {
            	isSuccessfullyLogin: true,
            	isUserLogin: function() {
            	    return this.isSuccessfullyLogin;
            	}
            };
            $provide.value('$location', locationMock);
            $provide.value('Authorization', authMock);
     }));

     beforeEach(inject(function( _$rootScope_) {
          $rootScope = _$rootScope_;
     }));


    it('should be defined', function() {
        expect(departmentApp).toBeDefined();
    });

    it('should test routes and load particular or html page view', inject(function ($route) {

    		expect($route.routes['/chooserManagment'].templateUrl).toEqual('commenViews/chooserManagment.html');

    		expect($route.routes['/logIn'].templateUrl).toEqual('commenViews/loginTemplate.html');

    		expect($route.routes[null].redirectTo).toEqual('chooserManagment');
    }));

    it('should redirect path to login view when user in not log in', function() {
        authMock.isSuccessfullyLogin = false;
        spyOn(authMock , 'isUserLogin').and.callThrough();
        spyOn(locationMock, 'path');

        $rootScope.$broadcast("$routeChangeStart", "next");

        expect(authMock.isUserLogin).toHaveBeenCalled()
        expect(locationMock.path).toHaveBeenCalledWith("/logIn");

    })

     it('should NOT redirect path to login view when user in log in', function() {
            authMock.isSuccessfullyLogin = true;
            spyOn(authMock , 'isUserLogin').and.callThrough();
            spyOn(locationMock, 'path');

            $rootScope.$broadcast("$routeChangeStart", "next");

            expect(authMock.isUserLogin).toHaveBeenCalled()
            expect(locationMock.path).not.toHaveBeenCalledWith("/logIn");

        })
});