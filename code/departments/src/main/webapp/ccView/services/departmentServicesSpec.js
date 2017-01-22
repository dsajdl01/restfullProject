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
        var staffId = 1;
        var response = { department: {depId: 101, name: "IT", createdBy: "David" } } ;
        httpBackend.whenGET('/department/rest/dep/getListDepartment?staffId=' + staffId).respond(200, response);
        httpBackend.expectGET('/department/rest/dep/getListDepartment?staffId=' + staffId);

        var callbackCalled = false;
        var callback = function(responce) {
           	expect(responce).toBeTruthy;
          	callbackCalled = true;
        };

        service.getDepartmentList(staffId, callback);
        httpBackend.flush();
        expect(callbackCalled).toBeTruthy();
    });

    it('should return false when getDepartmentList() is called and any errors occurs', function() {
            var staffId  = 1;
            httpBackend.whenGET('/department/rest/dep/getListDepartment?staffId=' + staffId).respond(500, "some error");
            httpBackend.expectGET('/department/rest/dep/getListDepartment?staffId=' + staffId);

            var callbackCalled = false;
            var callback = function(responce) {
               	expect(responce).toBeFalsy();
              	callbackCalled = true;
            };

            service.getDepartmentList(staffId, callback);
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
        var dep = { "depId": 127, "depName": "Network team", "createdBy": 1}
         httpBackend.whenPUT('/department/rest/dep/createDepartment?staffId=' + 1, dep).respond(200, true);
         httpBackend.expectPUT('/department/rest/dep/createDepartment?staffId=' + 1, dep);

        var callbackCalled = false;
            var callback = function() {
                callbackCalled = true;
            };

        service.saveDepartment(127, "Network team", 1, callback, "DammyFunction");
        httpBackend.flush();
        expect(callbackCalled).toBeTruthy();
    });

    it('should not save department when saveDepartment() is called and any error occur', function(){
        var dep = { "depId": 127, "depName": "Network team", "createdBy": 1}
        httpBackend.whenPUT('/department/rest/dep/createDepartment?staffId=' + 1, dep).respond(500, false);
        httpBackend.expectPUT('/department/rest/dep/createDepartment?staffId=' + 1, dep);

        var callbackCalled = false;
        var callback = function() {
                  callbackCalled = true;
        };

        service.saveDepartment(127, "Network team", 1, "DammyFunction", callback);
        httpBackend.flush();
        expect(callbackCalled).toBeTruthy();
    });

    it('should return promise with department when getDepartment() is called', function(){
        var dep = { "depId": 127, "depName": "Network team", "createdBy": "David"};
        httpBackend.whenGET('/department/rest/dep?depId=' + 127).respond( 200, dep );
        httpBackend.expectGET('/department/rest/dep?depId=' + 127);


         service.getDepartment(127)
                .then(function(response) {
                expect(response.data).toEqual(dep);
         });

         httpBackend.flush();
    });

     it('should return promise with error when getDepartment() is called and faild', function(){
            httpBackend.whenGET('/department/rest/dep?depId=127').respond( 500, {"message": "some error"} );
            httpBackend.expectGET('/department/rest/dep?depId=127');

            service.getDepartment(127)
                 .then()
                 .catch(function (fail) {
                    expect(fail.status).toBe(500);
                    expect(fail.data).toEqual({"message": "some error"} );
            });

            httpBackend.flush();
     });
})