departmentApp.factory('login', [ '$http', function ($http) {
	return new login($http);
}]);

function login ($http) {

    var self = this;

    self.loginUser = function(email, password) {
        console.log(email, password);
        return $http ({
            method: "PUT",
            url: '/department/rest/user/login',
            headers: {
               "content-type": "application/json"
            },
            data: {
                email: email,
                password: password
            }
        });
    }
}