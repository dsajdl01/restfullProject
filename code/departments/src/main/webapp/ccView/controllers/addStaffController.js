controlCenterApp.controller('addStaffController', ['commonService', '$sessionStorage', 'DepService', 'toaster', '$location',
        function(commonService, $sessionStorage, depService, toaster, $location) {
        var self = this;
        self.origineStartDay = null;
        self.dateErrorMessage = null;

        self.init = function() {
            if ( commonService.selectedDepartment ) {
                self.depName = commonService.selectedDepartment.depName;
            }  else {
                var promise = depService.getDepartment($sessionStorage.depId);
                return promise
                    .then(function (data) {
                        self.depName = data.data.depName;
                    })
                    .catch( function(failure) {
                        toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while getting department id.",failure));
                        $location.path('/home');
                    })
            }
        }



        self.init();
}]);