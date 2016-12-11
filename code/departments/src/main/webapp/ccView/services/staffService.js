controlCenterApp.factory('staffService', [ '$http',
 function($http) {
	return new staffService($http);
}]);

function staffService($http)
{
    var self = this;

    self.doesEmailExist = function(email) {
        return $http ({
            method: "GET",
            url: '/department/rest/user/emailExist',
            params: { 'email': email }
        });
    }

    self.seveStaff = function(user, createrId) {
        return $http ({
            method: "PUT",
            url: '/department/rest/user/' + createrId + '/newStaff',
            params: { newStaff: user }
        });
    }
}