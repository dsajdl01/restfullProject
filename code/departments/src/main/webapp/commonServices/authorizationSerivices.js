departmentApp.factory('Authorization',[ 'commonServicesUser',
    function(commonServicesUser) {
	    return new Authorization(commonServicesUser);
}]);


function Authorization (commonServicesUser){

       var self = this;

       self.init = function() {
           // return commonServicesUser.isUserLogIn;
       }

    self.isLodIn = function() {
        return commonServicesUser.isUserLogIn;
    }
}