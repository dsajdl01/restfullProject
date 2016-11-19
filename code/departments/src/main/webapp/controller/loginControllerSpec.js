describe('Controller: loginController', function() {

       	var ctrl, locationMock, authorizationMock, sessionStorageMock, userName = "Fred Smith";

       	beforeEach(module('depApp'));

         var successPromise = {
            then: function (m) {
                 m( {data: { userId: 175, name: userName }} );
                return this;
            },
            catch: function (m) {
                // do nothing
            }
         };

         var failurePromise = {
             then: function (m) {
                 return this;
             },
             catch: function (m) {
                 m( {status: 400, data: { message: "Incorrect Email or Password" }} );
                 return this;
             }
        };

       	beforeEach(module(function($provide) {
       	    locationMock = {
       	        path: function(path) {}
       	    };

       	    authorizationMock = {
       	        success: true,
       	        isLogin: false,
       	        loginUser: function(email, password) {
       	            if ( this.success ) {
       	                return successPromise;
       	            } else {
       	                return failurePromise;
       	            }
       	        },
       	        logout: function(){},
       	        isUserLogin: function() {
       	            return this.isLogin;
       	        }
       	    };

            sessionStorageMock = {
                user: null
            };

       	    $provide.value('$sessionStorage', sessionStorageMock);
       	    $provide.value('Authorization', authorizationMock);
       	    $provide.value('$location', locationMock);
       	}));

       	beforeEach(inject(function($controller) {
            ctrl = $controller('loginController');
        }));

        it('should be defined - Controller', function() {
             expect(ctrl).toBeDefined();
        });

        it('should initialize user name when init() is called and user is login', function() {
            sessionStorageMock.user = { name: userName };
            authorizationMock.isLogin = true;
            ctrl.init();
            expect(ctrl.userName).toEqual(userName);
        });

        it('should call logout when user is not login or time expire and init() is called', function() {
             authorizationMock.isLogin = false;
             spyOn(ctrl, 'logout').and.callThrough();;
             spyOn(authorizationMock, 'logout');
             spyOn(locationMock, 'path');
             ctrl.init();
             expect(ctrl.logout).toHaveBeenCalled()
             expect(authorizationMock.logout).toHaveBeenCalled();
             expect(locationMock.path).toHaveBeenCalledWith('/logIn');
        });

        it('should redirect view to Chooser Management and set username when login() is called and is successful', function() {
             authorizationMock.success = true;
             ctrl.email = "fred@example.com";
             ctrl.password = "password";

             spyOn(authorizationMock , 'loginUser').and.callThrough();
             spyOn(locationMock, 'path');

             ctrl.login();

             expect(ctrl.userName).toEqual(userName);
             expect(authorizationMock.loginUser).toHaveBeenCalledWith("fred@example.com", "password");
             expect(locationMock.path).toHaveBeenCalledWith('/chooserManagment')
        });

        it('should redirect view to Chooser Management and set username when login() is called and is successful', function() {
             authorizationMock.success = false;
             ctrl.email = "fred@example.com";
             ctrl.password = "password";

             spyOn(authorizationMock , 'loginUser').and.callThrough();
             spyOn(authorizationMock, 'logout');

             ctrl.login();

             expect(ctrl.userName).toEqual("");
             expect(authorizationMock.loginUser).toHaveBeenCalledWith("fred@example.com", "password");
             expect(authorizationMock.logout).toHaveBeenCalled();
        });
});