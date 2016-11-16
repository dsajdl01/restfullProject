departmentApp.factory('Authorization',[ '$sessionStorage',
    function($sessionStorage) {
	    return new Authorization($sessionStorage);
}]);


// http://www.stefanoscerra.it/permission-based-auth-system-in-angularjs/
function Authorization ($sessionStorage){

       var self = this;

       self.init = function() {
           // return commonServicesUser.isUserLogIn;
       }

    self.isLodIn = function() {
        checkLoginTime();
        return $sessionStorage.user != null;
    }

    var checkLoginTime = function() {
        var diffMin = getDifferent();
        if ( diffMin > 2) {
            delete $sessionStorage.user;
            delete $sessionStorage.loginDay;
        }
    }

    var getDifferent = function(){
        var loginTime = new Date($sessionStorage.loginDay);
        var currentTime = new Date();

        var diffMs = (currentTime - loginTime); // milliseconds between now & loginTime

        var diffHrs = Math.round((diffMs % 86400000) / 3600000); // hours
        var diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000); // minutes

        return  (diffHrs == 0) ? diffMins : ((diffHrs * 60) + diffMins);
    }
}