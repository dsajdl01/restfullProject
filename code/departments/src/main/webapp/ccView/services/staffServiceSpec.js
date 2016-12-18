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

    it('should return true if the email already exist and doesEmailExist is called', function() {
        var email = "example@co.uk";
        httpBackend.whenGET('/department/rest/user/emailExist?email=' + email ).respond(200, true );
        httpBackend.expectGET('/department/rest/user/emailExist?email=' + email);

        service.doesEmailExist(email)
            .then(function(response) {
                expect(response.data).toBeTruthy();
            });
         httpBackend.flush();
    });

    it('should return false if the email does not exist and doesEmailExist is called', function(){
            var email = "example@co.uk";
            httpBackend.whenGET('/department/rest/user/emailExist?email=' + email ).respond(200, false);
            httpBackend.expectGET('/department/rest/user/emailExist?email=' + email);
                service.doesEmailExist(email)
                    .then(function(response) {
                    expect(response.data).toBeFalsy();
                });

            httpBackend.flush();
     });

    it('should be catch error when error occur and doesEmailExist is called', function(){
             var email = "example@co.uk";
             httpBackend.whenGET('/department/rest/user/emailExist?email=' + email ).respond(500, {"message": "some error"});
             httpBackend.expectGET('/department/rest/user/emailExist?email=' + email);

             service.doesEmailExist(email)
                    .then(function(response) {})
                    .catch(function (fail) {
                         expect(fail.status).toBe(500);
                         expect(fail.data).toEqual({"message": "some error"} );
             });
             httpBackend.flush();
    });

    it('should save staff personal data when saveStaff() is called', function(){
         var data = {fullName: "Fred Smith", dob: "1990-01-01"};
         httpBackend.whenPUT( '/department/rest/user/' + 3 + '/addNewStaff', data).respond(200, {});
         httpBackend.expectPUT( '/department/rest/user/' + 3 + '/addNewStaff', data );

         var itShouldBeCalled = false;
             service.saveStaff(data, 3)
                    .then(function(response) {
                    itShouldBeCalled = true;
             });

          httpBackend.flush();
          expect(itShouldBeCalled).toBeTruthy();
    });

    it('should NOT save staff personal data and catch error when saveStaff() is called and failed', function(){
        var data = {fullName: "Fred Smith", dob: "1990-01-01"};
        httpBackend.whenPUT( '/department/rest/user/' + 3 + '/addNewStaff', data ).respond(500, {"message": "some error"});
        httpBackend.expectPUT( '/department/rest/user/' + 3 + '/addNewStaff', data );

        var itShouldBeCalled = false;
              service.saveStaff(data, 3)
                 .then(function(response) {})
                 .catch(function (fail) {
                    expect(fail.status).toBe(500);
                    expect(fail.data).toEqual({"message": "some error"} );
                    itShouldBeCalled = true;
        });

        httpBackend.flush();
        expect(itShouldBeCalled).toBeTruthy();
    })
});