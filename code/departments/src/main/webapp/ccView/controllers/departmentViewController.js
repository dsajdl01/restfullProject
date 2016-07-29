
controlCenterApp.controller('departmentViewController', [ 'DepService', 'commonService',
		function(DepService, commonService){

	var self = this;
	self.depList;
	self.showPage = false;
	self.containTable = false;

	self.init = function(){
        DepService.getDepartmentList(function(listDepartment){
            if(listDepartment){
                self.showPage = true;
                self.containTable = commonService.isLengtsmallerOrEqualToZero(listDepartment.length);
                self.depList = listDepartment;
                console.log("self.depList: ", self.depList);
            } else {
                // display error message
            }
        });
	};
}]);