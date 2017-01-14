describe('Service: Authorization', function() {

    var service, httpBackend, sessionStorageMock;

   	beforeEach(module('depApp'));

   	beforeEach(inject(function($injector, _$httpBackend_, _$sessionStorage_) {
    	    sessionStorageMock = _$sessionStorage_;
        	httpBackend = _$httpBackend_;
        	service = $injector.get('Authorization');
    }));

    it('should be defined', function() {
      expect(service).toBeDefined();
    });

    it('should return false when user is NOT login and isUserLogInExist() is called', function() {
        expect(service.isUserLogin()).toBeFalsy();
    });

    it('should return false when user is login but time is out and isUserLogInExist() is called', function(){

        sessionStorageMock.user = { userId: 157, name: "Fred Smith" };
        sessionStorageMock.loginDay = getDateAndTimeOfSubstractingMinutes( 30 );
        expect(service.isUserLogin()).toBeFalsy();
    });

    it('should return true when user is login and time is not expired and isUserLogInExist() is called', function(){
        sessionStorageMock.user = { userId: 157, name: "Fred Smith" };
        sessionStorageMock.loginDay = getDateAndTimeOfSubstractingMinutes( 5 );
        expect(service.isUserLogin()).toBeTruthy();
    });

    it('should return promise with data when loginUser() is called', function(){
        var resUser = { userId: 157, name: "David" };
        var loginData = {email: "fred@example.com", password: "1835347"};
        httpBackend.whenPUT('/department/rest/user/login', loginData ).respond(200, resUser);
        httpBackend.whenPUT('/department/rest/user/login', loginData);

        service.loginUser("fred@example.com", "1835347" )
             .then(function(response) {
                 expect(response.data).toEqual(resUser);
        });

        httpBackend.flush();
    });

     it('should return promise with error message when loginUser() is called and password or mail is incorrect', function(){
            var errorMessage = "Email or password is incorrect";
            var loginData = {email: "fred@example.coms", password: "1835347"};
            httpBackend.whenPUT('/department/rest/user/login', loginData ).respond(400, errorMessage);
            httpBackend.whenPUT('/department/rest/user/login', loginData);

            service.loginUser("fred@example.coms", "1835347" )
                 .then()
                 .catch(function (fail) {
                   expect(fail.status).toBe(400);
                   expect(fail.data).toBe(errorMessage);
            });

            httpBackend.flush();
     })

    var getDateAndTimeOfSubstractingMinutes = function( minutes ) {
        var date = new Date();
        var currentDay = date;
        var oldDay = new Date( currentDay );
        oldDay.setMinutes( currentDay.getMinutes() - minutes );
        return oldDay;
    }
})