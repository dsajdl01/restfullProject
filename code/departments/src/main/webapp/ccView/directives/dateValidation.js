'use strict';
controlCenterApp.directive('dateValidation', ['$q',function($q) {
  return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elm, attrs, ngModel) {
              ngModel.$asyncValidators.uniqueStartDay = function(modelValue, viewValue) {

                    var controller = scope.$eval(attrs.dateValidation).ctrl;
                    var regEx = /^\d{4}-\d{2}-\d{2}$/;
                    controller.dateErrorMessage = false;

                    var setErrorMessage = function( message ) {
                        controller.dateErrorMessage = message;
                    }

                    if (controller.origineStartDay === viewValue) {
                        ngModel.$setValidity('dateValidation', true);
                    }
                    else {
                        var res = false;
                        if ( !viewValue.match(regEx) ) {
                            setErrorMessage("Invalid Date! Date must follow yyyy-mm-dd.");
                        } else {
                            var d;
                            if ( (d = new Date( viewValue ))|0 ) {
                                res =  d.toISOString().slice(0,10) == viewValue;
                            }
                            if ( !res ) setErrorMessage("Invalid Date! The date '" + viewValue + "' does not exist");
                        }
                      ngModel.$setValidity('dateValidation', res );
                  }
                  return $q.when([]);
              };
        }}
}]);
