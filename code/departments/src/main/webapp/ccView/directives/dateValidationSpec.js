'use strict';
describe('directive: dateValidation', function() {

     var elm, scope, form, stDay = "2015-01-01";

     beforeEach(module('ccApp'));

      beforeEach(inject(function($rootScope, $compile) {

            scope = $rootScope.$new();

            scope.ctrl = {
                  origineStartDay: stDay,
                  dateErrorMessage: false
            };

            elm = angular.element(
                 '<form name="form">' +
                        '<input  ' +
                        'date-validation="{ ctrl:ctrl }"' +
                        'name="dob"' +
                        'ng-model="model.dob"' +
                        '/>' +
                 '</form>');

            scope.model = {dob: null };

            elm = $compile(elm)(scope);
            form = scope.form;
      }));

      it('should set form dod invalid to false when form name is same as origin start day ', function(){
            form.dob.$setViewValue(stDay);
            scope.$digest();

            expect(scope.ctrl.origineStartDay).toEqual(stDay);
            expect(scope.ctrl.dateErrorMessage).toBeFalsy();
            expect(scope.model.dob).toEqual(stDay);
            expect(form.dob.$invalid).toBeFalsy();
      });

      it('should set form dod invalid to true and ser error message when form date is invalid', function(){
            form.dob.$setViewValue("2015/01/01");
            scope.$digest();

            expect(scope.ctrl.origineStartDay).toEqual(stDay);
            expect(scope.ctrl.dateErrorMessage).toEqual("Invalid Date! Date must follow yyyy-mm-dd.");
            expect(scope.model.dob).toEqual("2015/01/01");
            expect(form.dob.$invalid).toBeTruthy();
      });

      it('should set form dod invalid to true and ser error message when form date does not exist ', function(){
             form.dob.$setViewValue("2015-29-02"); // It should be yyyy-mm-dd
             scope.$digest();

             expect(scope.ctrl.origineStartDay).toEqual(stDay);
             expect(scope.ctrl.dateErrorMessage).toEqual("Invalid Date! The date '2015-29-02' does not exist");
             expect(scope.model.dob).toEqual("2015-29-02");
             expect(form.dob.$invalid).toBeTruthy();
      });

      it('should set form dod invalid to true and ser error message when form date does not exist II', function(){
             form.dob.$setViewValue("2015-02-29"); // It is not leap year
             scope.$digest();

             expect(scope.ctrl.origineStartDay).toEqual(stDay);
             expect(scope.ctrl.dateErrorMessage).toEqual("Invalid Date! The date '2015-02-29' does not exist");
             expect(scope.model.dob).toEqual("2015-02-29");
             expect(form.dob.$invalid).toBeTruthy();
      });

      it('should set form dod invalid to true and ser error message when form date does not exist III', function(){
            form.dob.$setViewValue("2016-07-32"); // It is not leap year
            scope.$digest();

            expect(scope.ctrl.origineStartDay).toEqual(stDay);
            expect(scope.ctrl.dateErrorMessage).toEqual("Invalid Date! The date '2016-07-32' does not exist");
            expect(scope.model.dob).toEqual("2016-07-32");
            expect(form.dob.$invalid).toBeTruthy();
      });

      it('should set form dod invalid to true and ser error message when form date does not exist III', function(){
            form.dob.$setViewValue("2016-07-22"); // It is not leap year
            scope.$digest();

            expect(scope.ctrl.origineStartDay).toEqual(stDay);
            expect(scope.ctrl.dateErrorMessage).toBeFalsy();
            expect(scope.model.dob).toEqual("2016-07-22");
            expect(form.dob.$invalid).toBeFalsy();
       });
});