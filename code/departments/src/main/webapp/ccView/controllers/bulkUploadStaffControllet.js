controlCenterApp.controller('bulkUploadStaffController', ['commonService',
        function(commonService){
        var self = this;
        self.showPage = false;
        self.departmentName = null;
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
        };

        self.init();
}]);