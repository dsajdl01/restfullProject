controlCenterApp.factory('commonService', [ '$http', function($http){
	return new commonService($http);
}]);

function commonService ($http){

    var self = this;

    self.init = function(){
        self.departmentList = [];
        self.selectedDepartment = null;
    }

    self.setDepartmentList = function(departments){
        self.departmentList = [];
        for(var ixd = 0; ixd < departments.length; ixd++){
            addDepartmentToList(departments[ixd])
        }
    }

    var addDepartmentToList = function(dep){
        var department = {
            depId: dep.depId,
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
        return length <= 0 ;
    }

    self.init();
}