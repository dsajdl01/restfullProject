
controlCenterApp.controller('departmentViewController', [ 'DepService', 'commonService', 'modalDialogBoxService', 'toaster', '$location',
		function(DepService, commonService, modalDialogBoxService,  toaster, $location){

	var self = this;
	self.depList;
	self.showPage = false;
	self.containTable = false;
	self.rdbDepValue = "";
	self.texts = "harvard."

	self.init = function(){
	    console.log("know running....");
        DepService.getDepartmentList(function(listDepartment){
            if(listDepartment){
                self.showPage = true;
                self.containTable = commonService.isLengtsmallerOrEqualToZero(listDepartment.length);
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

	self.modifyDepartemnt = function( depId ) {
	    if ( isDepartmentSelected(depId) ){
	        getSelectedDepartment( depId );
	        modalDialogBoxService.setTemplate("ccView/views/commonTemplateView.html");
            modalDialogBoxService.shareModalData = {
                    templateTitle: "Modify Department",
                    templateUrl: "ccView/views/modifyDepartmentTemplate.html"
            };

            modalDialogBoxService.notify = function() {};
            modalDialogBoxService.showDialog();
        }
	    console.log( "self.rdbDepValue: ", depId );
	    self.rdbDepValue;
	}

	self.bulkAddStaff = function(depId){
	    if ( isDepartmentSelected(depId) ){
	        getSelectedDepartment( depId );
	        $location.path('/bulkUploadStaff');
        }
	}

	var isDepartmentSelected = function(depId){
	     if (depId == ""  || depId == null ) {
	        toaster.pop("warning", "Please select depertment.")
	        return false;
	     }
	     return true;
	}

	self.addStaff = function(depId){
	    if ( isDepartmentSelected(depId) ) {
	        getSelectedDepartment( depId );
            modalDialogBoxService.setTemplate("ccView/views/commonTemplateView.html");
            modalDialogBoxService.shareModalData = {
                templateTitle: "Add Staff to " +  commonService.selectedDepartment.depName,
                templateUrl: "ccView/views/addSingleStaffTamplate.html"
            };

            modalDialogBoxService.notify = function() {};
            modalDialogBoxService.showDialog();
        }
	}

	var getSelectedDepartment = function( depId ){
	    for ( var inx = 0; inx < commonService.departmentList.length; inx++){
	        if ( commonService.departmentList[inx].depId == depId ) {
	            commonService.selectedDepartment = commonService.departmentList[inx];
	            return;
	        }
	    }
	    return;
	}
}]);