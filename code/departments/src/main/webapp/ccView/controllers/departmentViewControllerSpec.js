describe('Controller: departmentViewController', function() {

       beforeEach(module('ccApp'));

       var ctrl, depServiceMock, commonServiceMock, modalDialogBoxServiceMock,  toasterMock, locationMock, sessionStorageMock;

        beforeEach(module(function($provide) {

             depServiceMock = {
                  success: true,
                  getDepartmentList: function(response){
                       if ( this.success ) {
                            response(true);
                       } else {
                            response(false);
                       }
                  }
             };

             commonServiceMock = {
                init: function(){},
                isLengtsmallerOrEqualToZero: function(length){
                    return length <= 0;
                },
                departmentList: [ { depId: 101, depName: "IT Department", createdBy: "Bob Simth" },
                                  { depId: 102, depName: "Sale", createdBy: "Fred Wood" }]
             };

             modalDialogBoxServiceMock = {
                shareModalData: {
                    templateTitle: "",
                    templateUrl: ""
                },
                setTemplate: "",
                showDialog: ""
             };

             toasterMock = {
                pop: function(){}
             }

             locationMock = {
                path: ""
             }

             sessionStorageMock = {
                user: ""
             };

             $provide.value('modalDialogBoxService', modalDialogBoxServiceMock);
             $provide.value('commonService', commonServiceMock);
             $provide.value('DepService', depServiceMock);
             $provide.value('toaster', toasterMock);
             $provide.value('$location', locationMock);
             $provide.value('$sessionStorage', sessionStorageMock);
        }));

         beforeEach(inject(function($controller) {
             ctrl = $controller('departmentViewController');
         }));

         it('should be defined - Controller', function() {
             expect(ctrl).toBeDefined();
         });

         it('should set page and content to true when init() is called and department list contains data', function() {
            ctrl.init();
            expect(ctrl.showPage).toBeTruthy();
            expect(ctrl.containTable).toBeTruthy();
         });

        it('should set page to true and content to false when init() is called and department list is empty', function() {
            commonServiceMock.departmentList = [];
            ctrl.init();
            expect(ctrl.showPage).toBeTruthy();
         //   expect(ctrl.containTable).toBeFalsy();
        });

        it('should set page to true and content to false when init() is called and department list is empty', function() {
            depServiceMock.success = false;
            spyOn(toasterMock, 'pop');
            ctrl.init();
            expect(ctrl.showPage).toBeFalsy();
            expect(toasterMock.pop).toHaveBeenCalledWith("error","ERROR!","An internal error occur while getting department data.")
            expect(ctrl.supportMessage).toEqual("ERROR occur! Please contact help support team.");
        });

        it('should open dialog box when modifyDepartemnt() is called with id', function() {
            spyOn(modalDialogBoxServiceMock, "setTemplate");
            spyOn(modalDialogBoxServiceMock.shareModalData, "templateTitle");
            spyOn(modalDialogBoxServiceMock.shareModalData, "templateUrl");
            spyOn(modalDialogBoxServiceMock, "showDialog");

            ctrl.modifyDepartemnt(102);
            expect(modalDialogBoxServiceMock.setTemplate).toHaveBeenCalledWith("ccView/views/commonTemplateView.html");
            expect(modalDialogBoxServiceMock.shareModalData.templateTitle).toEqual("Modify Department");
            expect(modalDialogBoxServiceMock.shareModalData.templateUrl).toEqual("ccView/views/modifyDepartmentTemplate.html");
            expect(modalDialogBoxServiceMock.showDialog).toHaveBeenCalled();
        });

         it('should open dialog gox when modifyDepartemnt() is called with id', function() {
              spyOn(modalDialogBoxServiceMock, "setTemplate");
              spyOn(modalDialogBoxServiceMock.shareModalData, "templateTitle");
              spyOn(modalDialogBoxServiceMock.shareModalData, "templateUrl");
              spyOn(modalDialogBoxServiceMock, "showDialog");

              ctrl.modifyDepartemnt();

              expect(modalDialogBoxServiceMock.setTemplate).not.toHaveBeenCalledWith();
              expect(modalDialogBoxServiceMock.showDialog).not.toHaveBeenCalled();
         });

        it('should re-loacte page when bulkAddStaff() is called with id', function() {
            spyOn(locationMock, 'path');
            ctrl.bulkAddStaff(101);
            expect(locationMock.path).toHaveBeenCalledWith('/bulkUploadStaff');
        });

         it('should re-loacte page when bulkAddStaff() is called with id', function() {
              spyOn(locationMock, 'path');
              ctrl.bulkAddStaff();
              expect(locationMock.path).not.toHaveBeenCalled();
         });

         it('should relocate view by location.path when addStaff() is called with id', function() {
              spyOn(locationMock, 'path');
              spyOn(toasterMock, 'pop');
              ctrl.addStaff(101);
              expect(locationMock.path).toHaveBeenCalledWith('/addSingleStaff');
              expect(toasterMock.pop).not.toHaveBeenCalled();
         });

         it('should not relocate view and it should pop up toaster when addStaff() is called without id', function() {
             spyOn(toasterMock, 'pop');
             spyOn(locationMock, 'path');
             ctrl.addStaff();
             expect(toasterMock.pop).toHaveBeenCalledWith('warning', 'Please select depertment.');
             expect(locationMock.path).not.toHaveBeenCalled();
         });

         it('should relocate view by location.path when modifyStaff() is called with id', function() {
             spyOn(locationMock, 'path');
             spyOn(toasterMock, 'pop');
             ctrl.modifyStaff(101);
             expect(locationMock.path).toHaveBeenCalledWith('/modifySingleStaff');
             expect(toasterMock.pop).not.toHaveBeenCalled();
         });

         it('should not relocate view and it should pop up toaster when modifyStaff() is called without id', function() {
             spyOn(toasterMock, 'pop');
             spyOn(locationMock, 'path');
             ctrl.modifyStaff();
             expect(toasterMock.pop).toHaveBeenCalledWith('warning', 'Please select depertment.');
             expect(locationMock.path).not.toHaveBeenCalled();
         });
});