describe('Controller: commonTemplateController', function() {

       beforeEach(module('ccApp'));

       var ctrl, modalDialogBoxServiceMock;

        beforeEach(module(function($provide) {
            modalDialogBoxServiceMock = {
                   shareModalData: {
                       templateTitle: "Home Page",
                       templateUrl: "../template/index.html"
                   },
                   hideDialog: function(){}
            };

            $provide.value('modalDialogBoxService', modalDialogBoxServiceMock);
        }));

         beforeEach(inject(function($controller) {
             ctrl = $controller('commonTemplateController');
         }));

         it('should be defined - Controller', function() {
             expect(ctrl).toBeDefined();
         });

         it('should set variables when init() is called', function() {
            expect(ctrl.templateUrl).toEqual(modalDialogBoxServiceMock.shareModalData.templateUrl);
            expect(ctrl.templateTitle).toEqual(modalDialogBoxServiceMock.shareModalData.templateTitle);
         });

         it('should call hideDialog function when close() function is called', function() {
            spyOn(modalDialogBoxServiceMock, 'hideDialog');
            ctrl.close();
            expect(modalDialogBoxServiceMock.hideDialog).toHaveBeenCalled();
         });
})