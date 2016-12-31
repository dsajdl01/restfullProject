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
            headers: { "content-type": "application/json" },
            params: { 'email': email }
        });
    }

    self.saveStaff = function(user, depId) {
        return $http  ({
            method: "PUT",
            url: '/department/rest/user/' + depId + '/addNewStaff',
            headers: { "content-type": "application/json" },
            data: user
        });
    }

    self.searchForStaff = function(depId, searchValue, type ) {
         return $http  ({
              method: "POST",
              url: '/department/rest/user/' + depId + '/searchForStaff',
              params: { 'searchValue': searchValue, "type": type }
         });
    }

    self.modifyStaff = function(staff) {
        return $http  ({
                    method: "PUT",
                    url: '/department/rest/user/modifyStaff',
                    headers: { "content-type": "application/json" },
                    data: staff
                });
    }
}