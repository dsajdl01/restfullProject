departmentApp.factory('login',[ function () {
	return new login();
}]);

function login () {

    var self = this;

    self.loginUser = function(email, password) {
        console.log(email, password);
    }
}