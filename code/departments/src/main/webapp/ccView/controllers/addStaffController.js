controlCenterApp.controller('addStaffController', ['commonService', '$sessionStorage', 'staffService', 'DepService', 'toaster', '$location',
        function(commonService, $sessionStorage, staffService, depService, toaster, $location) {
        var self = this;
        self.origineStartDay = null;
        self.dateErrorMessage = null;
        self.originelEmail = null;
        self.user  = {};

        self.init = function() {
            self.user.startDay = getCurrentDay();
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

        self.save = function() {
            var promise = staffService.saveStaff( self.user, $sessionStorage.depId );
            return promise
                .then(function (){
                    toaster.pop("success","Done","User " + self.user.fullName + " is successfully saved");
                    $location.path('/home');
                })
                .catch( function(failure) {
                    toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while getting department id.",failure));
                    $location.path('/home');
                })
        }

       var getCurrentDay = function () {
            var dateTime = new Date();
            var date = dateTime.getDate();
            var month = ( dateTime.getMonth() + 1 );
            var year =  dateTime.getFullYear();
            return year + "-" + addZeroIfMissing(month) + "-" + addZeroIfMissing(date);
       }

       var addZeroIfMissing = function(val) {
            val = val.toString();
            return (val.length == 1 ) ? "0"+val : val;
       }

        self.init();
}]);