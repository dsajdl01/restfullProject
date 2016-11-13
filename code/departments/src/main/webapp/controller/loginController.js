departmentApp.controller('loginController', ['commonServicesUser',
        function(commonServicesUser){
        var self = this;
        self.email = null;
        self.password = null;

        self.login = function() {

             console.log("login values:", self.email, self.password);

        }
}]);