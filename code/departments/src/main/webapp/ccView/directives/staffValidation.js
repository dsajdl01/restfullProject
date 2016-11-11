controlCenterApp.directive('staffValidation', ['staffService', '$q', function(staffService, $q) {
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, elm, attrs, ngModel) {
			ngModel.$asyncValidators.uniqueStaff = function(modelValue, viewValue) {

				var controller = scope.$eval(attrs.staffValidation).ctrl;

				// Avoid a call to rest layer when editing and user has not changed details
				if (controller.originalEmail === viewValue) {
					ngModel.$setValidity('staffValidation', true);
					return $q.when([]);  // Empty promise, since we have to return a promise from a validator directive.
				}

				return staffService.doesStaffExist(viewValue, function (exists) {
					ngModel.$setValidity('staffValidation', !exists);
				});
			};
		}
	}
}]);