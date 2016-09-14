controlCenterApp.controller('departmentCreaterController', ['DepService', 'commonService','modalDialogBoxService', 'toaster', '$scope',
        function(depService, commonService, modalDialogBoxService, toaster, $scope ){

    var self = this;
    self.test = "hello world.";
    self.btnName = "Save";
    self.originalDepName = "";

    self.init = function (){
        if ( commonService.selectedDepartment ) {
            self.depName = commonService.selectedDepartment.depName;
            self.originalDepName = self.depName;
        }
    }

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
    }

    self.modify = function() {
        var userId = 1; // later get logon user
        console.log(commonService.selectedDepartment, "\nin sd jhfdhhfdghakhgfhgf", commonService.selectedDepartment.depId);
        depService.saveDepartment(commonService.selectedDepartment.depId, self.depName, userId, function() {
            toaster.pop("success", "Done",  self.depName + " is successfully added to the system.");
            location.reload();
        },
        function(error) {
            toaster.pop("error","ERROR!","An internal error occer while saving .");
        });
        modalDialogBoxService.hideDialog();

        console.log(commonService.selectedDepartment);
    }

    var refreshTable = function(){
        depService.getDepartmentList(function(){

        })
    }

    self.onChange = function() {
        self.btnName = "Save";
    }
}]);