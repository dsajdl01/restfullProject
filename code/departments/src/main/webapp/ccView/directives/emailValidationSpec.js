'use strict';
describe('directive: emailValidation', function() {

    var elm, scope, $httpBackend, toasterMock, form, email = "fred.smith@example.com";

     beforeEach(module('ccApp'));

     beforeEach(module(function($provide) {

        toasterMock = {
            pop: ""
        };

        $provide.value('toaster', toasterMock);
     }));

     beforeEach(inject(function($injector) {
           $httpBackend = $injector.get('$httpBackend');
     }));

     beforeEach(inject(function($rootScope, $compile) {

        scope = $rootScope.$new();

        scope.ctrl = {
             originalEmail: email
        };

        elm = angular.element(
            '<form name="form">' +
                '<input  ' +
                    'email-validation="{ctrl:ctrl}"' +
                    'name="email"' +
                    'ng-model="model.email"' +
                '/>' +
            '</form>');

        scope.model = {email: null };

        elm = $compile(elm)(scope);
        form = scope.form;

     }));

     it('should set email validation to false when origin value is the same as a new email value', function () {
        form.email.$setViewValue(email);
        scope.$digest();

        expect(scope.ctrl.originalEmail).toEqual(email);
        expect(scope.model.email).toEqual(email);
        expect(form.email.$invalid).toBeFalsy();
     });

    it('should call doesEmailExist() and set validation to true when email exist in the system', function () {
        var diffEmail = "diff.email@exmple.co.uk";
        $httpBackend.whenGET('/department/rest/user/emailExist?email=' + diffEmail).respond(200, true);
        $httpBackend.expectGET('/department/rest/user/emailExist?email=' +  diffEmail);

        form.email.$setViewValue(diffEmail);
        scope.$digest();
        $httpBackend.flush();

        expect(scope.ctrl.originalEmail).toEqual(email);
        expect(scope.model.email).toEqual(diffEmail);
        expect(form.email.$invalid).toBeTruthy();
    });

    it('should call doesEmailExist() and set validation to true when email exist in the system', function () {
        var diffEmail = "diff.email@exmple.co.uk";
        $httpBackend.whenGET('/department/rest/user/emailExist?email=' + diffEmail).respond(200, false);
        $httpBackend.expectGET('/department/rest/user/emailExist?email=' +  diffEmail);

        form.email.$setViewValue(diffEmail);
        scope.$digest();
        $httpBackend.flush();

        expect(scope.ctrl.originalEmail).toEqual(email);
        expect(scope.model.email).toEqual(diffEmail);
        expect(form.email.$invalid).toBeFalsy();
    });

    it('should call doesEmailExist(), pop up toaster and set validation to true when error occur in the system', function () {
        spyOn(toasterMock, 'pop');
        var diffEmail = "diff.email@exmple.co.uk";
        $httpBackend.whenGET('/department/rest/user/emailExist?email=' + diffEmail).respond(500, {"message": "some error occur"});
        $httpBackend.expectGET('/department/rest/user/emailExist?email=' +  diffEmail);

        form.email.$setViewValue(diffEmail);
        scope.$digest();
        $httpBackend.flush();

        expect(scope.ctrl.originalEmail).toEqual(email);
        expect(scope.model.email).toEqual(diffEmail);
        expect(form.email.$invalid).toBeTruthy();
        expect(toasterMock.pop).toHaveBeenCalledWith("error", "ERROR", "Error occur while compering email. Error message: some error occur")
    });
});