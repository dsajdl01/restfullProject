
controlCenterApp.factory('DepService', [ '$http',
 function($http) {
	return new DepService($http);
}]);

function DepService($http)
 {
    var self = this;

    self.getDepartmentList = function(successCallback){
    		var Url = '/department/rest/dep/getListDepartment';
    		return $http(
    				{
    					method: 'GET',
    					url: Url
    				})
    				.then(function (response)
    				{
    					console.log("response: ", response.data);
    					successCallback(response.data.department);
    				},
    				function errorCallBack(error){
    				    successCallback(false);
    				});
    	}

    self.doesDepartmentExist = function(depName, callback){
           return $http(
                       {
                           url: '/department/rest/dep/checkDepartmentName',
                           params: {'depName': depName}
                       }).then(function (response) {
                           callback(response.data);
                       }
                       // Errors are ignored here, since it's just a convenience operation
           );
    }

    self.saveDepartment = function (depName, userId, callbackSuccess, callbackFailure) {
         var restUrl = '/department/rest/dep/createDepartment';
         var newDepartment = { "depId": null, "depName": depName, "createdBy": userId };
         return $http(
            {
                method: 'PUT',
                url: restUrl,
                headers : {
                    "content-type": "application/json"
                },
                data:  newDepartment
            }).
            then(function(response) {
                callbackSuccess(response.data);
            }).
            then(function(response) {
                callbackFailure(response.data);
            });
    }
 }