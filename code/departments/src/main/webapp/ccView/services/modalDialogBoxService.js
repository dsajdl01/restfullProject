controlCenterApp.factory('modalDialogBoxService', ['$modal', function($modal){
	return new modalDialogBoxService($modal);
}]);

function modalDialogBoxService($modal){
	 var self = this;

	 self.modalData = {
	 	backdrop: 'static',
		size: 'md',
	 	show: false
	 };

	 self.setTemplate = function(template)
	 {
	 	self.modalData.templateUrl = template;
	 };

	 self.notify = function(someObject) {};

	 self.shareModalData = {};

	 self.modalInstance = null;

	 self.showDialog = function()
	 {
	 	if(!self.modalData.templateUrl){
	 		throw "No template set for modal dialog";
	 	}

	 	self.modalInstance = $modal(self.modalData);
	 	self.modalInstance.$promise.then(self.modalInstance.show);
	 };

	 self.hideDialog = function()
	 {
	 	self.modalInstance.hide();
	 }

	 self.notifyAndHide = function(object)
	 {
	 	self.notify(object);
	 	self.hideDialog();
	 };
};