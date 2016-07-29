controlCenterApp.factory('commonService',[ function(){
	return new commonService();
}]);

function commonService (){

    var self = this;

    self.isLengtsmallerOrEqualToZero = function(length){
        return 0 <= length;
    }
}