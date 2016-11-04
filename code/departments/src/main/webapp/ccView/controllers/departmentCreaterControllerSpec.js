describe('Controller: departmentCreaterController', function() {

       beforeEach(module('ccApp'));

       var ctrl, depServiceMock, commonServiceMock, modalDialogBoxServiceMock, toasterMock;

       beforeEach(module(function($provide) {

            modalDialogBoxServiceMock = {};

            commonServiceMock = {
                selectedDepartment: {
                    depName: "IT Department",
                    depId: 175,
                    createdBy: "David"
                }
            };

            depServiceMock = {

            };

            toasterMock = {};

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

});