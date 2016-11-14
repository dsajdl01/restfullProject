departmentApp.controller('loginController', ['commonServicesUser', 'login',
        function(commonServicesUser, login){
        var self = this;
        self.email = null;
        self.password = null;

        self.login = function() {


             console.log("login values:", self.email, self.password)
             var promise = login.loginUser(self.email, self.password);
             return promise
                .then(function (data) {
                    console.log(data);
                })
                .catch( function(failure) {
                    console.log(failure);
                })

        }
}]);