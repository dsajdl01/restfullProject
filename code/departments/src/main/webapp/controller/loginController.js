departmentApp.controller('loginController', ['$location', 'Authorization', '$sessionStorage', 'toaster',
        function($location, authorization, $sessionStorage, toaster) {
        var self = this;
        self.email = null;
        self.password = null;
        self.userName = "";
        self.errorMessage = null;

        self.init = function () {
            if ( authorization.isUserLogin() ) {
              self.userName =  $sessionStorage.user.name;
            } else {
                self.logout();
            }
        };

        self.login = function() {
             self.errorMessage = null
             var promise = authorization.loginUser(self.email, self.password);
             return promise
                .then(function (data) {
                    $sessionStorage.user = data.data;
                    $sessionStorage.loginDay = new Date();
                    self.userName = data.data.name;
                    $location.path('/chooserManagment');
                })
                .catch( function(failure) {
                    authorization.logout();
                    if ( failure.status == 400 ) {
                        self.errorMessage =  failure.data.message;
                    } else {
                        var message;
                        if ( failure.data && failure.data.message ) {
                            message = "An internal error occur while login. Error message: " + failure.data.message;
                        } else {
                            message = "An internal error occur while login. Error status: "  + failure.status;
                        }
                        toaster.pop("error","ERROR!", message );
                    }
                })
        };

        self.logout = function() {
            authorization.logout();
            $location.path('/logIn');
        };

        self.init();
}]);