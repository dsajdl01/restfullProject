controlCenterApp.controller('departmentCreaterController', ['DepService', 'toaster', function(depService, toaster){

    var self = this;
    self.test = "hello world.";
    self.btnName = "Save";
    self.originalDepName = "";

    self.save = function(){
        console.log("depName: ", self.depName );
        var userId = 1; // later get logon user
        depService.saveDepartment(self.depName, userId, function(respponce) {
            self.btnName = "Done";
            var name = self.depName;
            self.depName = null;
            toaster.pop("success", "Done", name + " is successfully added to the system.");
        },
        function(error){
            toaster.pop("error","ERROR!","An internal error occer while getting depatrment data.");
        });
    }

    self.onChange = function() {
        self.btnName = "Save";
    }
}]);