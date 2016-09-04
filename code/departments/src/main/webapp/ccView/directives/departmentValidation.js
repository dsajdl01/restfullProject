controlCenterApp.directive('departmentValidation', ['DepService', '$q', function(depService, $q) {
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, elm, attrs, ngModel) {
			ngModel.$asyncValidators.uniqueDepartment = function(modelValue, viewValue) {

				var controller = scope.$eval(attrs.departmentValidation).ctrl;

				// Avoid a call to rest layer when editing and user has not changed details
				if (controller.originalDepName === viewValue) {
						ngModel.$setValidity('departmentValidation', true);
						return $q.when([]);  // Empty promise, since we have to return a promise from a validator directive.
				}

			    depService.doesDepartmentExist(viewValue, function (exists) {
                      ngModel.$setValidity('departmentValidation', !exists);
				});
				return $q.resolve();
			};
		}
	}
}]);