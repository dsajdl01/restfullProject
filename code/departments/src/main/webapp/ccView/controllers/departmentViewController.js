
controlCenterApp.controller('departmentViewController', [ 'DepService', 'commonService','toaster',
		function(DepService, commonService, toaster){

	var self = this;
	self.depList;
	self.showPage = false;
	self.containTable = false;

	self.init = function(){
        DepService.getDepartmentList(function(listDepartment){
            if(listDepartment){
                self.showPage = true;
                self.containTable = commonService.isLengtsmallerOrEqualToZero(listDepartment.length);
                listDepartment.push({'createdBy':"David Sajdl","delName":"Finance","depId":3});
                listDepartment.push({'createdBy':"David Sajdl","delName":"H&R","depId":4});
                listDepartment.push({'createdBy':"David Sajdl","delName":"Customer Service","depId":5});
                self.depList = listDepartment;
                console.log("self.depList: ", self.depList);
            } else {
                // display error message
                toaster.pop("error","ERROR!","An internal error occer while getting depatrment data.");
                self.showPage = false;
                self.supportMessage = "ERROR occur! Please contact help support team."
            }
        });
	};
}]);