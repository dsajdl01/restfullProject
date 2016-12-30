controlCenterApp.controller('modifyStaffController', ['modalDialogBoxService', 'commonService', '$sessionStorage', 'staffService', 'DepService', 'toaster', '$location',
        function(modalDialogBoxService, commonService, $sessionStorage, staffService, depService, toaster, $location) {

        var self = this;
        self.origineStartDay = null;
        self.dateErrorMessage = null;
        self.originelEmail = null;
        self.user  = {};

        self.originalEmail = null;
        var depId = null
        var originalFullname  = null;
        var originalDbo = null;
        var originalStaffDay = null;
        var originalPositio = null;
        var originalStaffEmail = null;
        var originalCommet = null;
        var originalLastDay =null;

        var searchedStaffResult = [];
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
                    depId = data.data.depId;

                })
                .catch( function(failure) {
                    toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while getting department id.",failure));
                    $location.path('/home');
                })
            }
        };

        self.getSelectedName = function(searchId) {
            self.showStaffForm = false;
            if (searchId == SEARCH_VALUE.NAME) {
                self.showFullnameField = true;
                self.showDOBField = false;
            } else if (searchId == SEARCH_VALUE.DOB ) {
                self.showFullnameField = false;
                self.showDOBField = true;
            } else {
                hideSearchInputFields();
            }
        };

        var hideSearchInputFields = function() {
            self.showFullnameField = false;
            self.showDOBField = false;
        }

        self.search = function(searchValue) {
            self.zeroStaffSearch = null;
            self.searchOption = null;
            console.log("searchValue: ", searchValue, " ctrl.searchName: ", self.searchName);
             var promise = staffService.searchForStaff($sessionStorage.depId, searchValue, self.searchName );
              return promise
                    .then(function (data) {
                    console.log(data);
                    if ( data.data.staffDetailsList.length >= 1 ) {
                        searchedStaffResult = data.data.staffDetailsList;
                        getStaffListAndDisplayDialogBox( searchedStaffResult );
                    } else {
                        self.zeroStaffSearch = "Nothing has been found for key world: " + searchValue
                    }
              })
              .catch( function(failure) {
                    toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while searching for staff.", failure));
                    self.homeLocation();
              })
        }

        self.homeLocation = function() {
            $location.path('/home');
        }

        var getStaffListAndDisplayDialogBox = function( listOfStaff ) {
            self.searchOption = []
            for (var i = 0; i < listOfStaff.length; i++ ){
                self.searchOption.push(getStaffObjOption(listOfStaff[i]));
            }
            dispalyDialogBox(self.searchOption);
        }

        var dispalyDialogBox = function(staffList) {
            modalDialogBoxService.setTemplate("ccView/views/staffListTemplate.html");
            modalDialogBoxService.shareModalData = {
                staffList: staffList
            };
            modalDialogBoxService.notify = function(staffId) {
                    console.log(staffId);
                    self.showStaffForm = true;
                    initialiseStaffVariables(getStaffObject(staffId))
                    hideSearchInputFields();
                    self.searchName = DEFAULT_VALUE;
                    self.onChange();
            }
            modalDialogBoxService.showDialog();
        }

        var initialiseStaffVariables = function(staff) {
            setOriginalValues(staff);
            self.user.staffId = staff.staffId;
            self.user.fullName = staff.fullName;
            self.user.dob = staff.dob;
            self.user.startDay = staff.startDay;
            self.user.position = staff.position;
            self.user.email = staff.staffEmail;
            self.user.comment = staff.comment;
            self.user.lastDay = staff.lastDay;
        }

        self.modifyStaff = function() {
            console.log(self.user);
            var promise = staffService.modifyStaff(self.user, depId);
            return promise
                  .then(function (){
                        toaster.pop("success","Done","User " + self.user.fullName + " is successfully saved");

                  })
                  .catch( function(failure) {
                       if ( failure.status == 400) {
                            self.innerValidationError = failure.data.message;
                       } else {
                            toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while modify staff name: " + originalFullname + ".",failure));
                            self.homeLocation();
                       }
                  })
        }

        var setOriginalValues = function( staff ) {
            self.originalEmail = staff.staffEmail;
            originalFullname  = staff.fullName;
            originalDbo = staff.dob;
            originalStaffDay = staff.startDay;
            originalPositio = staff.position;
            originalStaffEmail = staff.staffEmail;
            originalCommet = staff.comment;
            originalLastDay = staff.lastDay;
        }

        var getStaffObject = function(staffId) {
            for ( var i = 0; i < searchedStaffResult.length; i ++ ) {
                if ( searchedStaffResult[i].staffId == staffId ) {
                    return searchedStaffResult[i];
                }
            }
            return null;
        }

        self.onChange = function() {
            self.valuesAreAsOriginalValues = equalTo();
        }

        var getStaffObjOption = function(staff) {
            return {id: staff.staffId, name: staff.fullName, dob: staff.dob };
        }

        var initialiseValues = function() {
            self.namesToSearch =[{ name: "Full Name", id: SEARCH_VALUE.NAME }, { name: "Date of Birthday", id: SEARCH_VALUE.DOB }];
            self.searchName = "-1";
            self.searchOption = null;
            self.showStaffForm = false;
        }

        var equalTo = function() {
            return originalFullname ===  self.user.fullName && originalDbo === self.user.dob && originalStaffDay === self.user.startDay
            && originalPositio === self.user.position && self.originalEmail === self.user.email && originalCommet === self.user.comment && originalLastDay === self.user.lastDay;
        }

        self.init();
}]);