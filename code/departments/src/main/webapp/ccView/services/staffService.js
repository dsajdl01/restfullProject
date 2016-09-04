controlCenterApp.factory('staffService', [ '$http',
 function($http) {
	return new staffService($http);
}]);

function staffService($http)
{
    var self = this;

    self.doesStaffExist = function(callback){
        var url = "someURL";
        callback(true);
    }
}