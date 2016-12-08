describe('Controller: departmentCreaterController', function() {

       beforeEach(module('ccApp'));

       var ctrl, depServiceMock, commonServiceMock, modalDialogBoxServiceMock, toasterMock;

       beforeEach(module(function($provide) {

            modalDialogBoxServiceMock = {
                hideDialog: ""
            };

            commonServiceMock = {
                selectedDepartment: {
                    depName: "IT Department",
                    depId: 175,
                    createdBy: "David"
                },
                modifyDepartmentList: function(obj){}
            };

            depServiceMock = {
                success: true,
                saveDepartment: function(depId, name, userId, response, fail) {
                    if ( this.success ) {
                        response();
                    } else {
                        fail({ status: 500});
                    }
                },
            };

           toasterMock = {
                pop: ""
           };

           $provide.value('modalDialogBoxService', modalDialogBoxServiceMock);
           $provide.value('commonService', commonServiceMock);
           $provide.value('DepService', depServiceMock);
           $provide.value('toaster', toasterMock);
       }));

       beforeEach(inject(function($controller) {
            ctrl = $controller('departmentCreaterController');
       }));

       it('should be defined - Controller', function() {
            expect(ctrl).toBeDefined();
       });

       it('should initialize variable when init is called and selectedDepartment is defined', function(){
            ctrl.init();
            expect(ctrl.depName).toEqual(commonServiceMock.selectedDepartment.depName);
            expect(ctrl.originalDepName).toEqual(commonServiceMock.selectedDepartment.depName);
       });

       it('should initialize variable when init is called and selectedDepartment is defined', function(){
            commonServiceMock.selectedDepartment = null;
            ctrl.init();
            expect(ctrl.depName).toBeNull();
            expect(ctrl.originalDepName).toEqual("");
       });

       it('should save a new department name and pop up success toaster when save is called', function(){
            depServiceMock.success = true;
            spyOn(depServiceMock, "saveDepartment").and.callThrough();
            spyOn(toasterMock, 'pop');
            ctrl.depName = "Development Team";
            expect(ctrl.btnName).toEqual("Save");

            ctrl.save();
            expect(ctrl.depName).toBeNull();
            expect(depServiceMock.saveDepartment).toHaveBeenCalled();
            expect(toasterMock.pop).toHaveBeenCalledWith("success", "Done", "Development Team is successfully added to the system.");
            expect(ctrl.btnName).toEqual("Done");
       });

       it('should not save a new department name and pop up error toaster when save is called and fail to save', function(){
            depServiceMock.success = false;
            spyOn(depServiceMock, "saveDepartment").and.callThrough();
            spyOn(toasterMock, 'pop');
            ctrl.depName = "Development Team";
            expect(ctrl.btnName).toEqual("Save");

            ctrl.save();
            expect(ctrl.depName).toEqual("Development Team");
            expect(depServiceMock.saveDepartment).toHaveBeenCalled();
            expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR!", "An internal error occur while getting department data.");
            expect(ctrl.btnName).toEqual("Save");
       });

       it('should save modified department name and pop up success toaster when modify() is called', function(){
            depServiceMock.success = true;
            spyOn(depServiceMock, "saveDepartment").and.callThrough();
            spyOn(toasterMock, 'pop');
            spyOn(modalDialogBoxServiceMock, 'hideDialog');
            ctrl.depName = "Development Team";

            ctrl.modify();
            expect(depServiceMock.saveDepartment).toHaveBeenCalled();
            expect(toasterMock.pop).toHaveBeenCalledWith("success", "Done", "Development Team is successfully added to the system.");
            expect(modalDialogBoxServiceMock.hideDialog).toHaveBeenCalled();
       });

        it('should not save modified department name and pop up error toaster when modify() is called and fail', function(){
            depServiceMock.success = false;
            spyOn(depServiceMock, "saveDepartment").and.callThrough();
            spyOn(toasterMock, 'pop');
            spyOn(modalDialogBoxServiceMock, 'hideDialog');
            ctrl.depName = "Development Team";

            ctrl.modify();
            expect(depServiceMock.saveDepartment).toHaveBeenCalled();
            expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR!", "An internal error occur while saving. Status error: 500");
            expect(modalDialogBoxServiceMock.hideDialog).toHaveBeenCalled();
        });

        it('should change button name to save when onChange() is called', function(){
            ctrl.btnName = null;
            expect(ctrl.btnName).toBeNull();

            ctrl.onChange();
            expect(ctrl.btnName).toEqual("Save");
        })
});