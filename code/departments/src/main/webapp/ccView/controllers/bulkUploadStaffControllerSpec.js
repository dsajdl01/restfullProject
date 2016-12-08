describe('Controller: bulkUploadStaffController', function() {

       beforeEach(module('ccApp'));

       var ctrl, commonServiceMock;

       beforeEach(module(function($provide) {
       		commonServiceMock = {
       		    selectedDepartment: {
       		        depName: "IT Department",
       		        depId: 100
       		    }
       		};

       		$provide.value('commonService', commonServiceMock);
       }));

       beforeEach(inject(function($controller) {
               ctrl = $controller('bulkUploadStaffController');
       }));

       it('should be defined - Controller', function() {
               expect(ctrl).toBeDefined();
       });

       it("should set department name and set showPage to true when init() is called and selectedDepartment is set", function() {
            ctrl.init();

            expect(ctrl.departmentName).toEqual("IT Department")
            expect(ctrl.showPage).toBeTruthy();
       });

       it("should set showPage to false when init() is called and selectedDepartment depId is null", function() {
             commonServiceMock.selectedDepartment.depId = null;
             ctrl.init();

             expect(ctrl.showPage).toBeFalsy();
        });

        it("should set showPage to false when init() is called and selectedDepartment is null", function() {
             commonServiceMock.selectedDepartment = null;
             ctrl.init();

             expect(ctrl.showPage).toBeFalsy();
         });
});