describe('Controller: staffChooserController', function() {

       beforeEach(module('ccApp'));

       var ctrl, modalDialogBoxServiceMock;

       var staff = {staffId: 1, depId: 2, fullName: "John Smith", dbo: "1979-07-26", startDay:"2015-12-01",
                            lastDay: null, staffEmail: "john@smith.com", comment: "some comments"}

       beforeEach(module(function($provide) {

            modalDialogBoxServiceMock = {
                    shareModalData:  {
                        staffList: staff
                    },
                    notifyAndHide: function(value) {
                    },
                    hideDialog: function() {}
            };

             $provide.value('modalDialogBoxService', modalDialogBoxServiceMock);
       }));

       beforeEach(inject(function($controller) {
            ctrl = $controller('staffChooserController');
       }));

       it('should be defined - Controller', function() {
            expect(ctrl).toBeDefined();
       });

       it('should set staff list when init() is called', function() {
            ctrl.init();
            expect(ctrl.staffList).toEqual(staff);
       });

       it('should called notifyAndHide function with staffId when processStaff() is called', function() {
            spyOn(modalDialogBoxServiceMock, 'notifyAndHide');
            ctrl.processStaff(1);
            expect(modalDialogBoxServiceMock.notifyAndHide).toHaveBeenCalledWith(1);
       });

       it('should called hideDialog function when close() is called', function() {
            spyOn(modalDialogBoxServiceMock, 'hideDialog');
            ctrl.close();
            expect(modalDialogBoxServiceMock.hideDialog).toHaveBeenCalled();
       });
});