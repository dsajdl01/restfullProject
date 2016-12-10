controlCenterApp.factory('staffService', [ '$http',
 function($http) {
	return new staffService($http);
}]);

function staffService($http)
{
    var self = this;

    self.doesEmailExist = function(email){
        return $http ({
            method: "GET",
            url: '/department/rest/user/emailExist',
            params: { 'email': email }
        });
    }
}