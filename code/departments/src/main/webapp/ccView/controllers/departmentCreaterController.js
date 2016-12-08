controlCenterApp.controller('departmentCreaterController', ['DepService', 'commonService','modalDialogBoxService', 'toaster',
        function(depService, commonService, modalDialogBoxService, toaster){

    var self = this;
    self.btnName = "Save";
    self.depName = null;
    self.commonService = commonService;

    self.init = function (){
        self.depName = null;
        self.originalDepName = "";
        if ( self.commonService.selectedDepartment ) {
            self.depName = self.commonService.selectedDepartment.depName;
            self.originalDepName = self.depName;
        }
    };

    self.save = function(){
        var userId = 1; // later get logon user
        depService.saveDepartment(null, self.depName, userId, function() {
            self.btnName = "Done";
            var name = self.depName;
            self.depName = null;
            toaster.pop("success", "Done", name + " is successfully added to the system.");
        },
        function(){
            toaster.pop("error","ERROR!","An internal error occur while getting department data.");
        });
    };

    self.modify = function() {
        var userId = 1; // later get logon user
        depService.saveDepartment(self.commonService.selectedDepartment.depId, self.depName, userId, function() {
            self.commonService.modifyDepartmentList( getNewDepObject() );
            toaster.pop("success", "Done",  self.depName + " is successfully added to the system.");
        },
        function(error) {
            toaster.pop("error","ERROR!", UTILS.responseErrorHandler("An internal error occur while saving.", error));
        });
        modalDialogBoxService.hideDialog();
    };

    var getNewDepObject = function() {
        return { depId: self.commonService.selectedDepartment.depId, depName: self.depName, createdBy: self.commonService.selectedDepartment.createdBy };
    };

    self.onChange = function() {
        self.btnName = "Save";
    };

    self.init();

}]);