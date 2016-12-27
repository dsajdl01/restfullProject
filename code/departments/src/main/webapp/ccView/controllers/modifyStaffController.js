controlCenterApp.controller('modifyStaffController', ['commonService', '$sessionStorage', 'staffService', 'DepService', 'toaster', '$location',
        function(commonService, $sessionStorage, staffService, depService, toaster, $location) {

        var self = this;
        self.origineStartDay = null;
        self.dateErrorMessage = null;
        self.originelEmail = null;
        self.user  = {};
        var DEFAULT_VALUE = "-1";
        var SEARCH_VALUE = {
            NAME: "NAME",
            DOB: "DOB"
        }

        self.init = function() {
            initialiseValues();
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
        };

        self.getSelectedName = function(searchId) {
            if (searchId == SEARCH_VALUE.NAME) {
                self.showFullnameField = true;
                self.showDOBField = false;
            } else if (searchId == SEARCH_VALUE.DOB ) {
                self.showFullnameField = false;
                self.showDOBField = true;
            } else {
                self.showFullnameField = false;
                self.showDOBField = false;
            }
        };

        self.search = function(searchValue) {

            console.log("searchValue: ", searchValue, " ctrl.searchName: ", self.searchName);
             var promise = staffService.searchForStaff($sessionStorage.depId, searchValue, self.searchName );
              return promise
                    .then(function (data) {
                    console.log(data);

              })
              .catch( function(failure) {
                    toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while searching for staff.", failure));
                    $location.path('/home');
              })
        }

        var initialiseValues = function() {
            self.namesToSearch =[{ name: "Full Name", id: SEARCH_VALUE.NAME }, { name: "Date of Birthday", id: SEARCH_VALUE.DOB }];
            self.searchName = "-1";

        }

        self.init();
}]);