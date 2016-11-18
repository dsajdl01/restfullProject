departmentApp.factory('Authorization',[ '$sessionStorage', '$http',
    function($sessionStorage, $http) {
	    return new Authorization($sessionStorage, $http);
}]);


// http://www.stefanoscerra.it/permission-based-auth-system-in-angularjs/
function Authorization ($sessionStorage, $http){

       var self = this;

    self.isUserLogin = function() {
        if(!isUserLogInExist()) return false;
        checkIfLoginTimeHasNotExpire();
        return isUserLogInExist();
    }

    var isUserLogInExist = function () {
        return $sessionStorage.user != null;
    }

    var checkIfLoginTimeHasNotExpire = function() {
        var diffMin = calculateDifferentBetweenLoginAndNow();
        if ( diffMin > 15) {
            self.logout();
        } else {
            $sessionStorage.loginDay = new Date();
        }
    };

    self.loginUser = function(email, password) {
        return $http ({
            method: "PUT",
            url: '/department/rest/user/login',
            headers: { "content-type": "application/json" },
            data: { email: email, password: password }
        });
    };

    self.logout = function() {
        delete $sessionStorage.user;
        delete $sessionStorage.loginDay;
    };

    var calculateDifferentBetweenLoginAndNow = function(){
        var loginTime = new Date($sessionStorage.loginDay);
        var currentTime = new Date();

        var diffMs = (currentTime - loginTime); // milliseconds between now & loginTime

        var diffHrs = Math.round((diffMs % 86400000) / 3600000); // hours
        var diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000); // minutes

        return  (diffHrs == 0) ? diffMins : ((diffHrs * 60) + diffMins);
    }
}