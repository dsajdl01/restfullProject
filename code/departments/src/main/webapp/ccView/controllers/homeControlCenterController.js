
controlCenterApp.controller('homeControlCenterController', [ 'DepService',
		function(DepService){

	var self = this;
	self.depList;

	self.init = function(){
   /*     DepService.getDepartmentList(function(department){
            self.depList = department;
            console.log("self.depList: ", self.depList);
        });*/
	};

}]);