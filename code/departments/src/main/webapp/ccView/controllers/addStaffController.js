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
                var promise = depService.getDepartment($sessionStorage.depId, $sessionStorage.user.userId);
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
            self.innerValidationError = null;
            var promise = staffService.saveStaff( createStaffInstance(self.user), $sessionStorage.depId, $sessionStorage.user.userId );
            return promise
                .then(function (){
                    toaster.pop("success","Done","User " + self.user.fullName + " is successfully saved");
                    self.home();
                })
                .catch( function(failure) {
                    if ( failure.status == 400) {
                        self.innerValidationError = failure.data.message;
                    } else {
                        toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while getting department id.",failure));
                        self.home();
                    }
                })
        }

       self.home = function() {
            $location.path('/home');
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

       var createStaffInstance = function(user) {
              try {
                    var newStaffInstance = {
                         fullName: user.fullName.trim(),
                         dob: user.dob.trim(),
                         startDay: user.startDay.trim(),
                         position: user.position.trim(),
                         staffEmail: (user.staffEmail) ? user.staffEmail.trim() : "",
                         comment: (user.comment) ? user.comment.trim() : "",
                         loginEmail: user.loginEmail.trim(),
                         password: user.newPassword.trim(),
                     };
                    return newStaffInstance;
              } catch (e) {
                    return null
              }
       }

        self.init();
}]);