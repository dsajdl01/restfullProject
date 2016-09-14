controlCenterApp.controller('bulkUploadStaffController', ['commonService', 'toaster',
        function(commonService, toaster ){
        var self = this;
        self.showPage = false;
        var commonService = commonService;

        self.init = function(){
            if(isDepatrmentSelected()){
                self.departmentName = commonService.selectedDepartment.depName;
                self.showPage = true;
            }
            else {
                self.showPage = false;
            }
        }

        var isDepatrmentSelected = function(){
            return commonService.selectedDepartment != null && commonService.selectedDepartment.depId != null ;
        }

}]);