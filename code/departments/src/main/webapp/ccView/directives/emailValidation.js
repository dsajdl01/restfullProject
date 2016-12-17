'use strict';
controlCenterApp.directive('emailValidation', ['$q','staffService', 'toaster', function($q, staffService, toaster) {
  return {
        restrict: 'A',
        require: 'ngModel',
        link: function(scope, elm, attrs, ngModel) {
              ngModel.$asyncValidators.uniqueStartDay = function(modelValue, viewValue) {

                    var controller = scope.$eval(attrs.emailValidation).ctrl;

                    if (controller.originalEmail === viewValue) {
                        ngModel.$setValidity('emailExitValidation', true);
                        return $q.when([]);
                    }

                    var promise = staffService.doesEmailExist(viewValue);
                    return promise
                        .then(function (data) {
                                ngModel.$setValidity('emailExitValidation', (!data.data));
                                return $q.when([]);
                        })
                        .catch( function(failure) {
                                toaster.pop("error", "ERROR", UTILS.responseErrorHandler("Error occur while compering email.",failure));
                                ngModel.$setValidity('emailExitValidation', false); //it can connot be save if email is not unique.
                                return $q.when([]);
                        })
              };
        }}
}]);