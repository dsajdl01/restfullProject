departmentApp.factory('commonServicesUser',[ function () {
	return new commonServicesUser();
}]);

function commonServicesUser () {

    var self = this;

    self.isUserLogIn = false;
}