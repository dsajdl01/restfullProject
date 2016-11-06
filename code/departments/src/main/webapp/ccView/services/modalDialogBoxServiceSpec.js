describe('Service: modalDialogBoxService', function() {

	var service, template = "path/to/some/template.html";

	beforeEach(module('ccApp'));

	// Mock the $modal provider, see http://www.sitepoint.com/mocking-dependencies-angularjs-tests/
	beforeEach(
		module(function($provide){
			$provide.provider('$modal', function(){

				this.$get = function() {
					function ModalFactory(config){
						var $modal = {};
						$modal.$promise = {
							then: function(callback){
								callback();
							}
						};
						$modal.show = jasmine.createSpy('show');
						$modal.hide = jasmine.createSpy('hide');
						return $modal;
					}
					return ModalFactory;
				}
			});
		})
	);

	beforeEach(inject(function(modalDialogBoxService)
	{
		service = modalDialogBoxService;
	}));

	it('should be defined', function()
	{
        expect(service).toBeDefined();
    });

    it('should be set data Model to show false', function()
    {
    	expect(service.modalData.show).toBeFalsy();
    	expect(service.modalInstance).toBeNull();
    	expect(service.shareModalData).toEqual({});
    });

    it('should set template when setTemplate is called', function()
    {
    	service.setTemplate(template);
    	expect(service.modalData.templateUrl).toEqual(template);
    });

    it('should test for template having been set in showDialog function', function()
    {
        try {
            service.showDialog();
        } catch (error){
        	expect(error).toEqual("No template set for modal dialog");
        }
    });

    it('should create instance and then call show when promis return', function()
    {
    	spyOn(service, 'modalInstance');
    	service.setTemplate(template);
    	service.showDialog();
    	expect(service.modalInstance.show).toHaveBeenCalled();
    });

    it('should create hide modal window when hideDialog is called', function()
    {
    	// fistly window needs to be open!
    	spyOn(service, 'modalInstance');
    	service.setTemplate(template);
    	service.showDialog();
    	expect(service.modalInstance.show).toHaveBeenCalled();

    	// close or hide window
    	service.hideDialog();
        expect(service.modalInstance.hide).toHaveBeenCalled();
    });

    it('should create hide the modal and call the notify method window when notifyAndHide is called', function()
    {
        spyOn(service, 'notify').and.callThrough();
        service.setTemplate(template);
        service.showDialog();
        service.notifyAndHide({someObject: true});
        expect(service.notify).toHaveBeenCalled();
        expect(service.modalInstance.hide).toHaveBeenCalled();
    });
});