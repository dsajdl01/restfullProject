describe('Controller: addStaffController', function() {

       beforeEach(module('ccApp'));

       var ctrl, commonServiceMock, sessionStorageMock, staffServiceMock, depServiceMock, toasterMock, locationMock;

       var successResponse = {
            then: function (m) {
                m( {data: { id: 175, depName: "Network Team" }} );
                return this;
            },
            catch: function (m) {
                // do nothing
            }
       };

       var failResponse = {
            then: function (m) {
                return this;
            },
       catch: function (m) {
            m( {status: 400, data: {} } );
                return this;
            }
       };

       var successSave = {
            then: function (m) {
                m();
                return this;
            },
            catch: function (m) {
                // do nothing
            }
       };

       var validationSave = {
          then: function (m) {
               return this;
          },
       catch: function (m) {
          m( {status: 400, data: { message: "Invalid staff email"} } );
            return this;
          }
       };

       var failSave = {
           then: function (m) {
                return this;
           },
       catch: function (m) {
           m( {status: 503, data: { message: "Unable to connect to database"} } );
                return this;
           }
       };

       beforeEach(module(function($provide) {

            commonServiceMock = {
                selectedDepartment: {
                    depName: "IT"
                }
            };

            sessionStorageMock = {
                depId: 118,
                user: { userId: 1 }
            };

            depServiceMock = {
                success: true,
                getDepartment: function(id, staffId) {
                    if ( this.success ) {
                        return successResponse;
                    } else {
                        return failResponse;
                    }
                }
            };

            staffServiceMock = {
                success: true,
                validationError: true,
                saveStaff: function(user, id) {
                    if ( this.success ) {
                        return successSave;
                    } else {
                        if (this.validationError) {
                            return validationSave;
                        } else {
                            return failSave;
                        }
                    }
                }
            }

            toasterMock = {
                pop: ""
            }

            locationMock = {
                path: ""
            }

            $provide.value('commonService', commonServiceMock);
            $provide.value('$sessionStorage', sessionStorageMock);
            $provide.value('staffService', staffServiceMock);
            $provide.value('commonService', commonServiceMock);
            $provide.value('DepService', depServiceMock);
            $provide.value('toaster', toasterMock);
            $provide.value('$location', locationMock);
       }));

       beforeEach(inject(function($controller) {
            ctrl = $controller('addStaffController');
       }));

       it('should be defined - Controller', function() {
            expect(ctrl).toBeDefined();
       });

       it('should assign depName when init() is called and common service contains selected dep name', function() {
            ctrl.init();
            expect(ctrl.depName).toEqual("IT");
            expect(ctrl.user.startDay.length).toBe(10);
       });

        it('should called getDepartment() when init() is called and common service does NOT contains selected dep name', function() {
            delete commonServiceMock.selectedDepartment;
            ctrl.init();
            expect(ctrl.depName).toEqual("Network Team");
            expect(ctrl.user.startDay.length).toBe(10);
        });

        it('should called getDepartment() when init() is called and common service does NOT contains selected dep name', function() {
            depServiceMock.success = false;
            ctrl.depName = null;
            spyOn(toasterMock, "pop")
            spyOn(locationMock, "path")
            delete commonServiceMock.selectedDepartment;

            ctrl.init();

            expect(ctrl.depName).toBeNull();
            expect(ctrl.user.startDay.length).toBe(10);
            expect(toasterMock.pop).toHaveBeenCalledWith('error', 'ERROR', 'Error occur while getting department id. Status error: 400');
        });

        it('should save staff details and pop up success toaster when save() is called', function() {
            generateStaff("Bob Marley", "ds@example.com", "some text");
            spyOn(staffServiceMock, "saveStaff").and.callThrough();
            spyOn(toasterMock, "pop");
            spyOn(locationMock, "path");

            ctrl.save();
            expect(staffServiceMock.saveStaff).toHaveBeenCalled();
            expect(locationMock.path).toHaveBeenCalledWith("/home");
            expect(toasterMock.pop).toHaveBeenCalledWith("success", "Done", "User Bob Marley is successfully saved");
        });

        it('should save staff details and pop up success toaster when save() is called II', function() {
            generateStaff("Yadira Diez", "", "");
            spyOn(staffServiceMock, "saveStaff").and.callThrough();
            spyOn(toasterMock, "pop");
            spyOn(locationMock, "path");

            ctrl.save();
            expect(staffServiceMock.saveStaff).toHaveBeenCalled();
            expect(locationMock.path).toHaveBeenCalledWith("/home");
            expect(toasterMock.pop).toHaveBeenCalledWith("success", "Done", "User Yadira Diez is successfully saved");
        });

         it('should NOT save staff details and pop up error toaster when save() is called and it failed', function() {
              staffServiceMock.success = false;
              staffServiceMock.validationError = true;
              generateStaff(null, "", "");
              spyOn(staffServiceMock, "saveStaff").and.callThrough();
              spyOn(toasterMock, "pop");
              spyOn(locationMock, "path");

              ctrl.save();
              expect(staffServiceMock.saveStaff).toHaveBeenCalled();
              expect(ctrl.innerValidationError).toEqual("Invalid staff email");
              expect(locationMock.path).not.toHaveBeenCalledWith("/home");
              expect(toasterMock.pop).not.toHaveBeenCalled();
         });


        it('should NOT save staff details and pop up error toaster when save() is called and it failed', function() {
            staffServiceMock.success = false;
            staffServiceMock.validationError = false;
            generateStaff(null, "", "");
            spyOn(staffServiceMock, "saveStaff").and.callThrough();
            spyOn(toasterMock, "pop");
            spyOn(locationMock, "path");

            ctrl.save();
            expect(staffServiceMock.saveStaff).toHaveBeenCalled();
            expect(ctrl.innerValidationError).toBeNull();
            expect(locationMock.path).toHaveBeenCalledWith("/home");
            expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR", "Error occur while getting department id. Error message: Unable to connect to database");
        });

        var generateStaff = function(name, email, comment) {
             ctrl.user.fullName = name;
             ctrl.user.dob = "1990-01-10";
             ctrl.user.startDay = "2016-01-01";
             ctrl.user.position = "IT";
             ctrl.user.email = email;
             ctrl.user.comment = comment;
             ctrl.user.loginEmail = "ds@co.uk";
             ctrl.user.newPassword = "password";
        }
})