controlCenterApp.controller('staffChooserController', ['modalDialogBoxService',
        function(modalDialogBoxService) {

        var self = this;

        self.init = function() {
            self.staffList = modalDialogBoxService.shareModalData.staffList;
        }

        self.processStaff = function(staffId) {
            console.log(staffId);
            modalDialogBoxService.notifyAndHide(staffId)
        }

        self.close = function() {
            modalDialogBoxService.hideDialog();
        }

        self.init();
}]);