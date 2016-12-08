
controlCenterApp.factory('DepService', [ '$http', 'commonService',
 function($http, commonService) {
	return new DepService($http, commonService);
}]);

function DepService($http, commonService)
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
    					commonService.setDepartmentList(response.data.department);
    					successCallback(true);
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

    self.saveDepartment = function (depId, depName, userId, callbackSuccess, callbackFailure) {
         var restUrl = '/department/rest/dep/createDepartment';
         var newDepartment = { "depId": depId, "depName": depName, "createdBy": userId };
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
                callbackSuccess();
            },
            function(response) {
                callbackFailure(response);
            });
    }

    self.getDepartment = function(depId) {
            return $http ({
                method: "GET",
                url: '/department/rest/dep',
                params: {'depId': depId}
            });
        };
 }