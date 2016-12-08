describe('Controller: loginController', function() {

       	var ctrl, locationMock, authorizationMock, sessionStorageMock, toasterMock, userName = "Fred Smith";

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

         var badRequestPromise = {
             then: function (m) {
                 return this;
             },
             catch: function (m) {
                 m( {status: 400, data: { message: "Incorrect Email or Password" }} );
                 return this;
             }
        };

        var failurePromise = {
            then: function (m) {
               return this;
            },
            catch: function (m) {
                m( { status: 500 });
                return this;
            }
        }

        var failurePromiseWithMessage = {
            then: function (m) {
                return this;
            },
            catch: function (m) {
                m( {status: 503, data: { message: "Enable to connect to database" }} );
                return this;
            }
        }


       	beforeEach(module(function($provide) {
       	    locationMock = {
       	        path: function(path) {}
       	    };

       	    authorizationMock = {
       	        success: true,
       	        isLogin: false,
       	        isBadRequest: true,
       	        withMessage: false,
       	        loginUser: function(email, password) {
       	            if ( this.success ) {
       	                return successPromise;
       	            } else {
       	                if ( this.isBadRequest ) {
       	                    return badRequestPromise;
       	                } else if ( !this.withMessage ) {
       	                    return failurePromise;
       	                } else {
       	                    return failurePromiseWithMessage;
       	                }
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

            toasterMock = {
                pop: ""
            };

       	    $provide.value('$sessionStorage', sessionStorageMock);
       	    $provide.value('Authorization', authorizationMock);
       	    $provide.value('$location', locationMock);
       	    $provide.value('toaster', toasterMock);
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
             authorizationMock.isBadRequest = true;
             ctrl.email = "fred@example.com";
             ctrl.password = "password";

             spyOn(authorizationMock , 'loginUser').and.callThrough();
             spyOn(authorizationMock, 'logout');
             spyOn(toasterMock, 'pop');
             ctrl.login();

             expect(ctrl.userName).toEqual("");
             expect(ctrl.errorMessage).toEqual("Incorrect Email or Password");
             expect(authorizationMock.loginUser).toHaveBeenCalledWith("fred@example.com", "password");
             expect(authorizationMock.logout).toHaveBeenCalled();
             expect(toasterMock.pop).not.toHaveBeenCalled();
        });

        it('should pop up error toaster message with error status code when login is called and failed', function() {
            authorizationMock.success = false;
            authorizationMock.isBadRequest = false;
            ctrl.email = "fred@example.com";
            ctrl.password = "password";

            spyOn(authorizationMock , 'loginUser').and.callThrough();
            spyOn(authorizationMock, 'logout');
            spyOn(toasterMock, 'pop');
            ctrl.login();

            expect(ctrl.userName).toEqual("");
            expect(ctrl.errorMessage).toBeNull();
            expect(authorizationMock.loginUser).toHaveBeenCalledWith("fred@example.com", "password");
            expect(authorizationMock.logout).toHaveBeenCalled();
            expect(toasterMock.pop).toHaveBeenCalledWith("error","ERROR!","An internal error occur while login. Error status: 500");
        });

        it('should pop up error toaster message when login is called and failed', function() {
            authorizationMock.success = false;
            authorizationMock.isBadRequest = false;
            authorizationMock.withMessage = true;
            ctrl.email = "fred@example.com";
            ctrl.password = "password";

            spyOn(authorizationMock , 'loginUser').and.callThrough();
            spyOn(authorizationMock, 'logout');
            spyOn(toasterMock, 'pop');
            ctrl.login();

            expect(ctrl.userName).toEqual("");
            expect(ctrl.errorMessage).toBeNull();
            expect(authorizationMock.loginUser).toHaveBeenCalledWith("fred@example.com", "password");
            expect(authorizationMock.logout).toHaveBeenCalled();
            expect(toasterMock.pop).toHaveBeenCalledWith("error","ERROR!","An internal error occur while login. Error message: Enable to connect to database");
        });
});