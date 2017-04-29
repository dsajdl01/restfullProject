
controlCenterApp.factory('DepService', [ '$http', 'commonService', function($http, commonService) {
    	return new DepService($http, commonService);
}]);

function DepService($http, commonService) {

    var self = this;

    self.getDepartmentList = function(staffId, successCallback) {
    	var Url = '/department/rest/dep/getListDepartment';
    	return $http(
    		{
    					method: 'GET',
    					url: Url,
    					params: {'staffId': staffId}
    		})
    		.then(function (response) {
    			commonService.setDepartmentList(response.data.department);
    			successCallback(true);
    		},
    		function errorCallBack(error) {
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

    self.saveDepartment = function (depId, depName, staffId, callbackSuccess, callbackFailure) {
         var restUrl = '/department/rest/dep/createDepartment';
         var newDepartment = { "depId": depId, "depName": depName, "createdBy": staffId };
         return $http(
            {
                method: 'PUT',
                url: restUrl,
                headers : {
                    "content-type": "application/json"
                },
                params: {"staffId": staffId},
                data:  newDepartment
            }).
            then(function(response) {
                callbackSuccess();
            },
            function(response) {
                callbackFailure(response);
            });
    };

    self.getDepartmentById = function(depId, staffId) {
            return $http ({
                method: "GET",
                url: '/department/rest/dep',
                params: {'depId': depId, 'staffId' : staffId}
            });
    };

    self.saveSelectedDepartmentId = function(depId) {
          return $http ({
                method: "POST",
                url: '/department/rest/dep/saveSelectedDepartmentId',
                params: {'depId': depId }
          });
    };

    self.getSelectedDepartment = function() {
        return $http ({
            method: "GET",
            url: '/department/rest/dep/getSelectedDepartment'
        });
    };
 }