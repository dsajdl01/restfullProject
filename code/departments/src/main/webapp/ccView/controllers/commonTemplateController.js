controlCenterApp.controller('commonTemplateController', ['modalDialogBoxService', function( modalDialogBoxService ){

        var self = this;

        self.init = function() {
            self.templateTitle = modalDialogBoxService.shareModalData.templateTitle;
            self.templateUrl = modalDialogBoxService.shareModalData.templateUrl;
        }

        self.close = function(){
            modalDialogBoxService.hideDialog();
        }

        self.init();
}]);