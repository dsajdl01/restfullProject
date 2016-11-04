controlCenterApp.controller('departmentCreaterController', ['DepService', 'commonService','modalDialogBoxService', 'toaster',
        function(depService, commonService, modalDialogBoxService, toaster){

    var self = this;
    self.btnName = "Save";
    self.originalDepName = "";
    self.commonService = commonService;

    self.init = function (){
        if ( self.commonService.selectedDepartment ) {
            self.depName = self.commonService.selectedDepartment.depName;
            self.originalDepName = self.depName;
        }
    };

    self.save = function(){
        console.log("depName: ", self.depName );
        var userId = 1; // later get logon user
        depService.saveDepartment(null, self.depName, userId, function(respponce) {
            self.btnName = "Done";
            var name = self.depName;
            self.depName = null;
            toaster.pop("success", "Done", name + " is successfully added to the system.");
        },
        function(error){
            toaster.pop("error","ERROR!","An internal error occer while getting depatrment data.");
        });
    };

    self.modify = function() {
        var userId = 1; // later get logon user
        depService.saveDepartment(self.commonService.selectedDepartment.depId, self.depName, userId, function() {
            self.commonService.modifyDepartmentList( getNewDepObject() );
            toaster.pop("success", "Done",  self.depName + " is successfully added to the system.");
        },
        function(error) {
            toaster.pop("error","ERROR!","An internal error occer while saving .");
        });
        modalDialogBoxService.hideDialog();
    };

    var getNewDepObject = function() {
        return { depId: self.commonService.selectedDepartment.depId, depName: self.depName, createdBy: self.commonService.selectedDepartment.createdBy };
    };

    var refreshTable = function(){
        depService.getDepartmentList(function(){

        })
    };

    self.onChange = function() {
        self.btnName = "Save";
    };

    self.init();

}]);