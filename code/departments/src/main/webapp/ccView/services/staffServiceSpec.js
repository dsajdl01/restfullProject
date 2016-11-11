describe('Service: staffService', function() {

	var service, httpBackend, commonServiceMock;

	beforeEach(module('ccApp'));

	beforeEach(inject(function($injector, _$httpBackend_, commonService) {
        commonServiceMock = commonService;
       	httpBackend = _$httpBackend_;
      	service = $injector.get('staffService');
    }));

    it('should be defined', function() {
         expect(service).toBeDefined();
    });
});