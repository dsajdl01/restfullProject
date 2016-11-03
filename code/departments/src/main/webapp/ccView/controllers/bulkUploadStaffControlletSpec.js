describe('Controller: bulkUploadStaffController', function() {

       beforeEach(module('ccApp'));

       var ctrl, commonServiceMock, toasterMock;

       beforeEach(module(function($provide)
       	{
       		commonServiceMock = {
       		};

            toasterMock = {};

       		$provide.value('commonService', commonServiceMock);
       		$provide.value('toaster', toasterMock);
       }));

       beforeEach(inject(function($controller) {
               ctrl = $controller('bulkUploadStaffController');
       }));

       it('should be defined - Controller', function() {
               expect(ctrl).toBeDefined();
        });
})