describe('Controller: modifyStaffController', function() {

       var ctrl, modalDialogBoxServiceMock, commonServiceMock, sessionStorageMock, staffServiceMock, depServiceMock, toasterMock, locationMock;

       beforeEach(module('ccApp'));

       var itDepartment = { depId: 1, depName: "IT Department", createdBy: "David Smith" };

       var staff = {staffId: 1, depId: 2, fullName: "John Smith", dob: "1979-07-26", startDay:"2015-12-01", position: "Java Developer",
                                   lastDay: null, staffEmail: "john@smith.com", comment: "some comments"};

       var DEFAULT_VALUE = "-1";
       var SEARCH_VALUE = {
            NAME: "NAME",
            DOB: "DOB"
       }

       var searchFoundStaff = {
           then: function (m) {
                 m( {data: { staffDetailsList: [ staff ] }} );
                 return this;
           },
           catch: function (m) {}
       };

       var searchValueResponse = {
            then: function (m) {
                 m( {data: "OK" } );
                 return this;
            },
            catch: function (m) {}
       }
       
       var searchNull = {
            then: function (m) {
                  m( {data: { staffDetailsList: [] }} );
                  return this;
            },
            catch: function (m) {}
       };

       var departmentSuccess = {
            then: function (m) {
               m( {data: itDepartment } );
               return this;
            },
            catch: function (m) {
                 // do nothing
            }
       };

       var sqlErrorResponse = {
            then: function (m) {
                 return this;
            },
            catch: function (m) {
                 m( {status: 504, data: { message: "Enable to connect to database"} } );
                 return this;
            }
       }

       var innerError = {
            then: function (m) {
                 return this;
            },
            catch: function (m) {
                 m( {status: 504 } );
                 return this;
            }
       };

       var badRequestResponse = {
            then: function (m) {
                  return this;
            },
            catch: function (m) {
                  m( {status: 400, data: { message: "Staff name must be at least 4 characters long"} } );
                  return this;
            }
       };

       beforeEach(module(function($provide) {

            modalDialogBoxServiceMock = {
                    shareModalData: {
                         staffList: "",
                    },
                    setTemplate: "",
                    showDialog: "",
                    notify: function(){},
            };

            commonServiceMock = {
                selectedDepartment: null
            };

            sessionStorageMock = {
                depId: 1
            };

            staffServiceMock = {
                success: true,
                staffFound: true,
                badRequest: true,
                modifySuccess: true,
                searchForStaff: function(depId, searchingName, searchType ){
                    if ( this.success ) {
                        if ( this.staffFound ) {
                            return searchFoundStaff
                        } else {
                            return searchNull;
                        }
                    } else {
                        return sqlErrorResponse;
                    }
                },
                modifyStaff: function(staff){
                    if (this.modifySuccess) {
                        return searchValueResponse
                    } else {
                        if ( this.badRequest ) {
                            return badRequestResponse;
                        } else {
                            return innerError
                        }
                    }
                }
            };

            depServiceMock = {
                success: true,
                getDepartment: function(depId) {
                    if ( this.success ) {
                        return departmentSuccess;
                    } else {
                        return sqlErrorResponse;
                    }
                }
            };

            toasterMock = {
                pop: ""
            };

            locationMock = {
                path: function(){}
            };

            $provide.value('modalDialogBoxService', modalDialogBoxServiceMock);
            $provide.value('commonService', commonServiceMock);
            $provide.value('$sessionStorage', sessionStorageMock);
            $provide.value('staffService', staffServiceMock);
            $provide.value('DepService', depServiceMock);
            $provide.value('toaster', toasterMock);
            $provide.value('$location', locationMock);
       }));

       beforeEach(inject(function($controller) {
            ctrl = $controller('modifyStaffController');
       }));

       it('should be defined - Controller', function() {
            expect(ctrl).toBeDefined();
       });

       it ('should not call getDepartment function when selectedDepartment holds values and init() is called', function() {
            commonServiceMock.selectedDepartment = itDepartment;
            spyOn(depServiceMock, 'getDepartment');

            ctrl.init();
            expect(ctrl.depName).toEqual("IT Department");
            expect(ctrl.user.depId).toEqual(1);
            expect(depServiceMock.getDepartment).not.toHaveBeenCalled();
            expect(ctrl.namesToSearch).toEqual([{ name: "Full Name", id: SEARCH_VALUE.NAME }, { name: "Date of Birthday", id: SEARCH_VALUE.DOB }]);
            expect(ctrl.searchingType).toEqual(DEFAULT_VALUE);
            expect(ctrl.searchOption).toBeNull();
            expect(ctrl.showStaffForm).toBeFalsy();
       });

       it ('should call getDepartment function when selectedDepartment does not holds values and init() is called', function() {
            spyOn(depServiceMock, 'getDepartment').and.callThrough();

            ctrl.init();
            expect(ctrl.depName).toEqual("IT Department");
            expect(ctrl.user.depId).toEqual(1);
            expect(depServiceMock.getDepartment).toHaveBeenCalledWith(1);

            expect(ctrl.namesToSearch).toEqual([{ name: "Full Name", id: SEARCH_VALUE.NAME }, { name: "Date of Birthday", id: SEARCH_VALUE.DOB }]);
            expect(ctrl.searchingType).toEqual(DEFAULT_VALUE);
            expect(ctrl.searchOption).toBeNull();
            expect(ctrl.showStaffForm).toBeFalsy();
       });

       it ('should call getDepartment function and pop up toaster when selectedDepartment does not holds values and fail to upload data', function() {
           depServiceMock.success = false;
           spyOn(depServiceMock, 'getDepartment').and.callThrough();
           spyOn(toasterMock, 'pop');

           ctrl.init();
           expect(ctrl.depName).toEqual("IT Department");
           expect(ctrl.user.depId).toEqual(1);
           expect(depServiceMock.getDepartment).toHaveBeenCalledWith(1);
           expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR", "Error occur while getting department id. Error message: Enable to connect to database")
       });

       it('should hide search variable or to set false when getSelectedValue() is called with unknown values', function(){
            ctrl.getSelectedValue(DEFAULT_VALUE);
            expect(ctrl.showFullnameField).toBeFalsy();
            expect(ctrl.showDOBField).toBeFalsy();
       });

       it('should show name field or set to true when getSelectedValue() is called with NAME type', function() {
            ctrl.getSelectedValue(SEARCH_VALUE.NAME);
            expect(ctrl.showFullnameField).toBeTruthy();
            expect(ctrl.showDOBField).toBeFalsy();
       });

       it('should show name field or set to true when getSelectedValue() is called with NAME type', function() {
            ctrl.getSelectedValue(SEARCH_VALUE.DOB);
            expect(ctrl.showFullnameField).toBeFalsy();
            expect(ctrl.showDOBField).toBeTruthy();
       });

       it('should call itDepartment and dispaly dialog box when search() is called and search value is found', function() {
            spyOn(modalDialogBoxServiceMock, "setTemplate");
            spyOn(modalDialogBoxServiceMock.shareModalData, "staffList");
            spyOn(modalDialogBoxServiceMock, 'notify').and.callThrough();

            modalDialogBoxServiceMock.showDialog = function () {
                        modalDialogBoxServiceMock.notify(1);
            };

            ctrl.init();
            ctrl.search("smith");

            expect(ctrl.showFullnameField).toBeFalsy();
            expect(ctrl.zeroStaffSearch).toBeNull();
            expect(ctrl.showDOBField).toBeFalsy();
            expect(ctrl.showStaffForm).toBeTruthy();
            expect(ctrl.searchingType).toEqual(DEFAULT_VALUE);
            expect(ctrl.originalEmail).toEqual("john@smith.com");
            expect(ctrl.user.staffId).toEqual(1);
            expect(ctrl.user.fullName).toEqual("John Smith");
            expect(ctrl.user.dob).toEqual("1979-07-26");
            expect(ctrl.user.startDay).toEqual("2015-12-01");
            expect(ctrl.user.position).toEqual("Java Developer");
            expect(ctrl.user.staffEmail).toEqual("john@smith.com");
            expect(ctrl.user.comment).toEqual("some comments");
            expect(ctrl.user.lastDay).toBeNull();
            expect(modalDialogBoxServiceMock.setTemplate).toHaveBeenCalledWith("ccView/views/staffListTemplate.html");
            expect(modalDialogBoxServiceMock.shareModalData.staffList).toEqual([{id: 1, name: "John Smith", dob: "1979-07-26"}]);
       });

       it('should call itDepartment and hide dispaly dialog box when search() is called and search value is found but notify is not right id', function() {
            spyOn(modalDialogBoxServiceMock, "setTemplate");
            spyOn(modalDialogBoxServiceMock.shareModalData, "staffList");
            spyOn(modalDialogBoxServiceMock, 'notify').and.callThrough();

            modalDialogBoxServiceMock.showDialog = function () {
                               modalDialogBoxServiceMock.notify(2);
            };

            ctrl.init();
            ctrl.search("smith");

            expect(ctrl.showFullnameField).toBeFalsy();
            expect(ctrl.showDOBField).toBeFalsy();
            expect(ctrl.showStaffForm).toBeFalsy();
            expect(ctrl.searchingType).toEqual(DEFAULT_VALUE);
            expect(ctrl.originalEmail).toBeNull();
            expect(ctrl.user.staffId).toBeUndefined();
            expect(ctrl.user.fullName).toBeUndefined();
            expect(ctrl.user.dob).toBeUndefined();
            expect(ctrl.user.startDay).toBeUndefined();
            expect(ctrl.user.position).toBeUndefined();
            expect(ctrl.user.staffEmail).toBeUndefined();
            expect(ctrl.user.comment).toBeUndefined();
            expect(ctrl.user.lastDay).toBeUndefined();
            expect(modalDialogBoxServiceMock.setTemplate).toHaveBeenCalledWith("ccView/views/staffListTemplate.html");
            expect(modalDialogBoxServiceMock.shareModalData.staffList).toEqual([{id: 1, name: "John Smith", dob: "1979-07-26"}]);
       });

       it('should call itDepartment and hide dispaly dialog box when search() is called and search value is found but notify is not right id', function() {
            spyOn(modalDialogBoxServiceMock, "setTemplate");
            staffServiceMock.staffFound = false;


            ctrl.init();
            ctrl.search("bob");

            expect(ctrl.zeroStaffSearch).toEqual("Nothing has been found for key world: bob")
            expect(modalDialogBoxServiceMock.setTemplate).not.toHaveBeenCalledWith();
       });

       it('should pop up toaster when search() is called and fail to connect to database', function() {
            spyOn(modalDialogBoxServiceMock, "setTemplate");
            staffServiceMock.success = false;
            spyOn(toasterMock, "pop");
            spyOn(locationMock, 'path');

            ctrl.init();
            ctrl.search("bob");

            expect(ctrl.zeroStaffSearch).toBeNull();
            expect(ctrl.searchOption).toBeNull();
            expect(modalDialogBoxServiceMock.setTemplate).not.toHaveBeenCalledWith();
            expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR", "Error occur while searching for staff. Error message: Enable to connect to database");
            expect(locationMock.path).toHaveBeenCalledWith("/home")
       });

        it('should pop up success toaster when modifyStaff() is called  and success to modify details', function() {
            ctrl.user = {
                fullName: "John Smith"
            }
            spyOn(toasterMock, "pop");
            spyOn(locationMock, 'path');

            ctrl.modifyStaff();
            expect(toasterMock.pop).toHaveBeenCalledWith("success", "Done", "User John Smith is successfully saved");
            expect(locationMock.path).toHaveBeenCalledWith("/home")
        });

       it('should initialise innerValidationError when modifyStaff() is called validation error occur (bad request)', function() {
            staffServiceMock.modifySuccess = false;

            var use = {
                fullName: "Jo"
            }

            spyOn(toasterMock, "pop");
            spyOn(locationMock, 'path');
            ctrl.modifyStaff();

            expect(ctrl.innerValidationError).toEqual("Staff name must be at least 4 characters long");
            expect(toasterMock.pop).not.toHaveBeenCalled();
            expect(locationMock.path).not.toHaveBeenCalledWith("/home")
       });

       it('should pop up error toaster when modifyStaff() is called and internal error occur', function() {
            spyOn(modalDialogBoxServiceMock, "setTemplate");
            spyOn(modalDialogBoxServiceMock.shareModalData, "staffList");
            spyOn(modalDialogBoxServiceMock, 'notify').and.callThrough();

            modalDialogBoxServiceMock.showDialog = function () {
                modalDialogBoxServiceMock.notify(1);
            };

            staffServiceMock.modifySuccess = false;
            staffServiceMock.badRequest = false;

            var use = {
                  fullName: "Jo Yang"
            }

            spyOn(toasterMock, "pop");
            spyOn(locationMock, 'path');

            ctrl.init();
            ctrl.search("smith");
            ctrl.modifyStaff();

            expect(ctrl.originalEmail).toEqual("john@smith.com");
            expect(ctrl.innerValidationError).toBeUndefined();
            expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR", "Error occur while modify staff name: John Smith. Status error: 504");
            expect(locationMock.path).toHaveBeenCalledWith("/home");
            expect(modalDialogBoxServiceMock.setTemplate).toHaveBeenCalledWith("ccView/views/staffListTemplate.html");
            expect(modalDialogBoxServiceMock.shareModalData.staffList).toEqual([{id: 1, name: "John Smith", dob: "1979-07-26"}]);
       });
});