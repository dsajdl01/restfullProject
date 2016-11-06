describe('Service: DepService', function() {

	var service, httpBackend, commonServiceMock;

	beforeEach(module('ccApp'));

	beforeEach(inject(function($injector, _$httpBackend_, commonService) {
	    commonServiceMock = commonService;
    	httpBackend = _$httpBackend_;
    	service = $injector.get('DepService');
    }));

    it('should be defined', function() {
        expect(service).toBeDefined();
    });

    it('should return true and set commonDepartmentList when getDepartmentList() is called', function() {

        var response = { department: {depId: 101, name: "IT", createdBy: "David" } } ;
        httpBackend.whenGET('/department/rest/dep/getListDepartment').respond(200, response);
        httpBackend.expectGET('/department/rest/dep/getListDepartment');

        var callbackCalled = false;
        var callback = function(responce) {
           	expect(responce).toBeTruthy;
          	callbackCalled = true;
        };

        service.getDepartmentList( callback);
        httpBackend.flush();
        expect(callbackCalled).toBeTruthy();
    });

    it('should return false when getDepartmentList() is called and any errors occurs', function() {
            httpBackend.whenGET('/department/rest/dep/getListDepartment').respond(500, "some error");
            httpBackend.expectGET('/department/rest/dep/getListDepartment');

            var callbackCalled = false;
            var callback = function(responce) {
               	expect(responce).toBeFalsy();
              	callbackCalled = true;
            };

            service.getDepartmentList( callback);
            httpBackend.flush();
            expect(callbackCalled).toBeTruthy();
    });

    it('should callback false when department does not exist', function(){
           httpBackend.whenGET('/department/rest/dep/checkDepartmentName?depName=Team').respond(200, false);
           httpBackend.expectGET('/department/rest/dep/checkDepartmentName?depName=Team');

           var callbackCalled = false;
           var callback = function(responce) {
               	expect(responce).toBeFalsy();
               	callbackCalled = true;
           };

           service.doesDepartmentExist("Team", callback);
           httpBackend.flush();
           expect(callbackCalled).toBeTruthy();
    });

    it('should callback true when department exists', function(){
        httpBackend.whenGET('/department/rest/dep/checkDepartmentName?depName=IT').respond(200, true);
        httpBackend.expectGET('/department/rest/dep/checkDepartmentName?depName=IT');

        var callbackCalled = false;
        var callback = function(responce) {
           	expect(responce).toBeTruthy();
           	callbackCalled = true;
        };

        service.doesDepartmentExist("IT", callback);
        httpBackend.flush();
        expect(callbackCalled).toBeTruthy();
    });

    it('should save department when saveDepartment() is called', function(){
        var dep = { "depId": 127, "depName": "Network team", "createdBy": "David"}
         httpBackend.whenPUT('/department/rest/dep/createDepartment', dep).respond(200, true);
         httpBackend.expectPUT('/department/rest/dep/createDepartment', dep);

          var callbackCalled = false;
                 var callback = function() {
                    callbackCalled = true;
                 };

          service.saveDepartment(127, "Network team", "David", callback, function(){});
          httpBackend.flush();
          expect(callbackCalled).toBeTruthy();
    });

    it('should not save department when saveDepartment() is called and any error occur', function(){
           var dep = { "depId": 127, "depName": "Network team", "createdBy": "David"}
           httpBackend.whenPUT('/department/rest/dep/createDepartment', dep).respond(500, false);
           httpBackend.expectPUT('/department/rest/dep/createDepartment', dep);

           var callbackCalled = false;
           var callback = function() {
                  callbackCalled = true;
           };

           service.saveDepartment(127, "Network team", "David", function(){}, callback);
           httpBackend.flush();
           expect(callbackCalled).toBeTruthy();
        });
})