departmentApp.controller('loginController', ['$location', 'login', '$sessionStorage',
        function($location, login, $sessionStorage){
        var self = this;
        self.email = null;
        self.password = null;
        self.userName = "";
        self.errorMessage = null;

        self.init = function () {
            if($sessionStorage.user != null) {
              self.userName =  $sessionStorage.user.name;
            }
        }
        self.login = function() {
             self.errorMessage = null
             console.log("login values:", self.email, self.password)
             var promise = login.loginUser(self.email, self.password);
             return promise
                .then(function (data) {
                    $sessionStorage.user = data.data;
                    $sessionStorage.loginDay = new Date();
                    self.userName = data.data.name;
                    $location.path('/chooserManagment');
                    console.log(data.data);
                })
                .catch( function(failure) {
                    delete $sessionStorage.user;
                    delete $sessionStorage.loginDay;
                    self.errorMessage =  failure.data.message;
                    console.log(failure);
                })

        }
        self.logout = function() {
            delete $sessionStorage.user;
            delete $sessionStorage.loginDay;
            $location.path('/logIn');
        }

        self.init();
}]);