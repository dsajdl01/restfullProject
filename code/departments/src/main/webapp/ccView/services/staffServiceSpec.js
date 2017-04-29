describe('Service: staffService', function() {

	var service, httpBackend, commonServiceMock;

	beforeEach(module('ccApp'));

	beforeEach(inject(function($injector, _$httpBackend_) {
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
                    .then()
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
                 .then()
                 .catch(function (fail) {
                    expect(fail.status).toBe(500);
                    expect(fail.data).toEqual({"message": "some error"} );
                    itShouldBeCalled = true;
        });

        httpBackend.flush();
        expect(itShouldBeCalled).toBeTruthy();
    });

    it('should return all staff name that include search value when searchForStaff() is called', function(){
        var depId = 2;
        var searchValue = "smith";
        var type = "NAME";
        var response = [{name: "Fred Smith", dob: "1980-02-02"}, {name: "Kate Smith", dob: "1992-03-01"}];
        httpBackend.whenPOST('/department/rest/user/' + depId + '/searchForStaff?searchValue=' + searchValue +'&type=' + type).respond(200, response);
        httpBackend.expectPOST('/department/rest/user/' + depId + '/searchForStaff?searchValue=' + searchValue +'&type=' + type);
        service.searchForStaff(depId, searchValue, type)
              .then(function(output) {
              expect(output.data).toEqual(response);
        });

        httpBackend.flush();
    });

    it('should return staff that match staff with date of birthday when searchForStaff() is called', function(){
        var depId = 2;
        var searchValue = "1980-02-02";
        var type = "DOB";
        var response = [{name: "Fred Smith", dob: "1980-02-02"}];
            httpBackend.whenPOST('/department/rest/user/' + depId + '/searchForStaff?searchValue=' + searchValue +'&type=' + type).respond(200, response);
            httpBackend.expectPOST('/department/rest/user/' + depId + '/searchForStaff?searchValue=' + searchValue +'&type=' + type);
            service.searchForStaff(depId, searchValue, type)
                  .then(function(output) {
                  expect(output.data).toEqual(response);
        });

        httpBackend.flush();
    });

    it('should return status error with message when searchForStaff() is called and failed', function(){
        var depId = 2;
        var searchValue = "1980-02-02";
        var type = "DOB";
        var response = {message: "Enable to connect to database"};
        httpBackend.whenPOST('/department/rest/user/' + depId + '/searchForStaff?searchValue=' + searchValue +'&type=' + type).respond(500, response);
        httpBackend.expectPOST('/department/rest/user/' + depId + '/searchForStaff?searchValue=' + searchValue +'&type=' + type);
        service.searchForStaff(depId, searchValue, type)
                  .then()
                  .catch(function (fail) {
                        expect(fail.status).toBe(500);
                        expect(fail.data.message).toEqual("Enable to connect to database");
        });
        httpBackend.flush();
    });

     it('should return staff that match staff with date of birthday when searchForStaff() is called', function(){
         var staff = {staffId: 1, depId: 2, fullName: "Fred Smith", dob: "1980-02-02", startDay: "2016-10-22", lastDay: null, staffEmail: null, comment: null };
         var response = "OK";
         httpBackend.whenPUT('/department/rest/user/modifyStaff', staff).respond(200, response);
         httpBackend.expectPUT('/department/rest/user/modifyStaff', staff);
              service.modifyStaff(staff)
                    .then(function(output) {
                    expect(output.data).toEqual("OK");
         });
         httpBackend.flush();
     });

    it('should return status error with message when modifyStaff() is called and failed', function(){
        var staff = {staffId: 1, depId: 2, fullName: "Fred Smith", dob: "1980-02-02", startDay: "2016-10-22", lastDay: null, staffEmail: null, comment: null };
        var response = {message: "Enable to connect to database"};
        httpBackend.whenPUT('/department/rest/user/modifyStaff', staff).respond(500, response);
        httpBackend.expectPUT('/department/rest/user/modifyStaff', staff);
        service.modifyStaff(staff)
             .then()
             .catch(function (fail) {
             expect(fail.status).toBe(500);
             expect(fail.data.message).toEqual("Enable to connect to database");
        });
        httpBackend.flush();
    });

    it('should get details of the login person when getLogInStaffDetails is called and user is login', function() {
        var loginDetails = {"userId": 2, "name": "Alex", "firstLogin": true};
        httpBackend.whenGET('/department/rest/user/getLoginStaff' ).respond(200, loginDetails);
        httpBackend.expectGET('/department/rest/user/getLoginStaff');

        var calledBack = false;
        service.getLogInStaffDetails()
             .then(function(response) {
                   calledBack = true;
                   expect(response.status).toBe(200);
                   expect(response.data).toEqual(loginDetails);
             });
        httpBackend.flush();
        expect(calledBack).toBeTruthy();
    });

    it('should catch error massage Plaese login when getLogInStaffDetails is called and user is NOT login', function() {
        var message = "Please login";
        httpBackend.whenGET('/department/rest/user/getLoginStaff' ).respond(403, message);
        httpBackend.expectGET('/department/rest/user/getLoginStaff');

        var calledBack = false;
        service.getLogInStaffDetails()
              .then()
              .catch(function(response){
                    calledBack = true;
                    expect(response.status).toBe(403);
                    expect(response.data).toEqual(message);
              });
        httpBackend.flush();
        expect(calledBack).toBeTruthy();
    });
});