departmentApp.controller('loginController', ['$location', 'Authorization', '$sessionStorage',
        function($location, authorization, $sessionStorage){
        var self = this;
        self.email = null;
        self.password = null;
        self.userName = "";
        self.errorMessage = null;

        self.init = function () {
            if ( authorization.isUserLogin() ) {
                console.log($sessionStorage.user);
              self.userName =  $sessionStorage.user.name;
            } else {
                self.logout();
            }
        };

        self.login = function() {
             self.errorMessage = null
             console.log("login values:", self.email, self.password)
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
                    self.errorMessage =  failure.data.message;
                })

        };

        self.logout = function() {
            authorization.logout();
            $location.path('/logIn');
        };

        self.init();
}]);