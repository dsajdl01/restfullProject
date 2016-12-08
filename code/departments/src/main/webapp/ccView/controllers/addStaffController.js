controlCenterApp.controller('addStaffController', ['commonService', '$sessionStorage', 'DepService',
        function(commonService, $sessionStorage, depService){
        var self = this;

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
                    })

            }
        }

        self.init();
}]);