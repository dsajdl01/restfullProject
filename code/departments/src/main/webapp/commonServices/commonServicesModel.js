controlCenterApp.factory('commonService',[ function(){
	return new commonService();
}]);

function commonService (){

    var self = this;

    self.init = function(){
        self.departmentList = [];
        self.selectedDepartment = null;
    }

    self.setDepartmentList = function(departments){
        for(var ixd = 0; ixd < departments.length; ixd++){
            addDepartmentToList(departments[ixd])
        }
    }

    addDepartmentToList = function(dep){
        var department = {
            depId: (dep.depId) ? dep.depId : null,
            depName: (dep.depName) ? dep.depName : null,
            createdBy: (dep.createdBy) ? dep.createdBy : null
        }
        self.departmentList.push(department);
    }

    self.modifyDepartmentList = function(department) {
        for(var ixd = 0; ixd < self.departmentList.length; ixd++){
            if(self.departmentList[ixd].depId == department.depId){
                self.departmentList[ixd].depName = department.depName;
                self.departmentList[ixd].createdBy = department.createdBy;
                return;
            }
        }
    }

    self.isLengtsmallerOrEqualToZero = function(length){
        return 0 <= length;
    }
}