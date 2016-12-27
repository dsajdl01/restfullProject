
controlCenterApp.controller('departmentViewController', [ 'DepService', 'commonService', 'modalDialogBoxService', 'toaster', '$location', '$sessionStorage',
		function(DepService, commonService, modalDialogBoxService,  toaster, $location, $sessionStorage){

	var self = this;
	self.depList;
	self.showPage = false;
	self.containTable = false;
	self.rdbDepValue = "";
	self.texts = "harvard."
	self.commonService = commonService;

	self.init = function() {
	    var user = $sessionStorage.user;
	    self.commonService.init();
        delete $sessionStorage.depId;
        DepService.getDepartmentList(function(responce) {
            if ( responce ) {
                self.showPage = true;
                self.containTable = !(commonService.isLengtsmallerOrEqualToZero(self.commonService.departmentList.length));
            } else {
                // display error message
                toaster.pop("error","ERROR!","An internal error occur while getting department data.");
                self.showPage = false;
                self.supportMessage = "ERROR occur! Please contact help support team.";
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
            modalDialogBoxService.showDialog();
        }
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
	        $sessionStorage.depId =  depId;
	        getSelectedDepartment( depId );
	        $location.path('/addSingleStaff')
        }
	}

	self.modifyStaff = function(depId){
	    if (isDepartmentSelected(depId) ) {
	        $sessionStorage.depId =  depId;
            getSelectedDepartment( depId );
            $location.path('/modifySingleStaff')
	    }
	}

	var getSelectedDepartment = function( depId ){
	    for ( var inx = 0; inx < commonService.departmentList.length; inx++){
	        if ( commonService.departmentList[inx].depId == depId ) {
	            commonService.selectedDepartment = commonService.departmentList[inx];
	            return;
	        }
	    }
	}

	self.init();
}]);