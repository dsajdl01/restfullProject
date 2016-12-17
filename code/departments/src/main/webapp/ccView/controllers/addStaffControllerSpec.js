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

       var failSave = {
           then: function (m) {
                return this;
           },
       catch: function (m) {
           m( {status: 400, data: { message: "Unable to connect to database"} } );
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
                depId: 118
            };

            depServiceMock = {
                success: true,
                getDepartment: function(id) {
                    if ( this.success ) {
                        return successResponse;
                    } else {
                        return failResponse;
                    }
                }
            };

            staffServiceMock = {
                success: true,
                saveStaff: function(user, id) {
                    if ( this.success ) {
                        return successSave;
                    } else {
                        return failSave;
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
            ctrl.user.fullName = "Bob Marley";
            spyOn(staffServiceMock, "saveStaff").and.callThrough();
            spyOn(toasterMock, "pop");
            spyOn(locationMock, "path");

            ctrl.save();
            expect(staffServiceMock.saveStaff).toHaveBeenCalled();
            expect(locationMock.path).toHaveBeenCalledWith("/home");
            expect(toasterMock.pop).toHaveBeenCalledWith("success", "Done", "User Bob Marley is successfully saved");
        });

        it('should save staff details and pop up success toaster when save() is called', function() {
            staffServiceMock.success = false;
            ctrl.user.fullName = "Bob Marley";
            spyOn(staffServiceMock, "saveStaff").and.callThrough();
            spyOn(toasterMock, "pop");
            spyOn(locationMock, "path");

            ctrl.save();
            expect(staffServiceMock.saveStaff).toHaveBeenCalled();
            expect(locationMock.path).toHaveBeenCalledWith("/home");
            expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR", "Error occur while getting department id. Error message: Unable to connect to database");
        });

})