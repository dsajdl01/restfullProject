'use strict';
describe('directive: departmentValidation', function() {

    var elm, scope, $httpBackend, form, commonServiceMock, depName = "IT";

     beforeEach(module('ccApp'));

     beforeEach(module(function($provide) {
          commonServiceMock = {   };
          $provide.value('commonService', commonServiceMock);
     }));

     beforeEach(inject(function($injector) {
           $httpBackend = $injector.get('$httpBackend');
     }));

     beforeEach(inject(function($rootScope, $compile) {

         scope = $rootScope.$new();

         scope.formCtrl = {
             originalDepName: depName
         };

         elm = angular.element(
             '<form name="form">' +
                 '<input  ' +
                    'department-validation="{ctrl:formCtrl}"' +
                    'name="departmentName"' +
                    'ng-model="model.departmentName"' +
                '/>' +
             '</form>');

         scope.model = {departmentName: null };

         elm = $compile(elm)(scope);
         form = scope.form;
    }));

    it('should set department name validation to false when origin value is the same as a new value - depName', function () {
          form.departmentName.$setViewValue(depName);
          scope.$digest();

          expect(scope.formCtrl.originalDepName).toEqual(depName);
          expect(scope.model.departmentName).toEqual(depName);
          expect(form.departmentName.$invalid).toBeFalsy();
    });

    it('should call doesDepartmentExist() and set validation to true when origin value is not the same as a new value - depName', function () {
        var diffDepartmentName = "Network";
        $httpBackend.whenGET('/department/rest/dep/checkDepartmentName?depName=' + diffDepartmentName).respond(200, true);
        $httpBackend.expectGET('/department/rest/dep/checkDepartmentName?depName=' +  diffDepartmentName);

        form.departmentName.$setViewValue(diffDepartmentName);
        scope.$digest();
        $httpBackend.flush();

        expect(scope.formCtrl.originalDepName).toEqual(depName);
        expect(scope.model.departmentName).toEqual(diffDepartmentName);
        expect(form.departmentName.$invalid).toBeTruthy();
    });

    it('should call doesDepartmentExist() set validation to false when origin value is not the same as a new value - depName', function () {
        var diffDepartmentName = "Network";
        $httpBackend.whenGET('/department/rest/dep/checkDepartmentName?depName=' + diffDepartmentName).respond(200, false);
        $httpBackend.expectGET('/department/rest/dep/checkDepartmentName?depName=' +  diffDepartmentName);

        form.departmentName.$setViewValue(diffDepartmentName);
        scope.$digest();
        $httpBackend.flush();

        expect(scope.formCtrl.originalDepName).toEqual(depName);
        expect(scope.model.departmentName).toEqual(diffDepartmentName);
        expect(form.departmentName.$invalid).toBeFalsy();
    });

});