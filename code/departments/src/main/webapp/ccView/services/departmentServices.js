
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
 }