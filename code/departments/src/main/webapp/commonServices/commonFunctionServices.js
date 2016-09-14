controlCenterApp.factory('commonService',[ function(){
	return new commonService();
}]);

function commonService (){

    var self = this;

    self.departmentList = null;
    self.selectedDepartment = null;

    self.isLengtsmallerOrEqualToZero = function(length){
        return 0 <= length;
    }
}