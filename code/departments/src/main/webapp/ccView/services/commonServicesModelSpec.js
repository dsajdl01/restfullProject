
describe('Service: commonService', function() {

    beforeEach(module('ccApp'));

    var departments = [ { depId: 101, depName: "H & R", createdBy: "Fred" },
                        { depId: 102, depName: "Sales", createdBy: "Bob" },
                        { depId: 103, depName: null, createdBy: null} ];

    beforeEach(inject(function($injector) {
    	service = $injector.get('commonService');
    }));

    it('should be defined', function() {
        expect(service).toBeDefined();
    });
    it('should set department list empty and selected dep to null when init() is called', function(){

        service.init();
        expect(service.departmentList).toEqual([]);
        expect(service.selectedDepartment).toBeNull();
    });

    it('should add department(s) to the list when setDepartmentList() is called', function() {
        service.init();
        expect(service.departmentList).toEqual([]);
        service.setDepartmentList(departments);
        expect(service.departmentList).toEqual(departments);
    });

    it('should modify department list when modifyDepartmentList() is called', function(){
        service.init();
        expect(service.departmentList).toEqual([]);
        service.setDepartmentList(departments);
        expect(service.departmentList).toEqual(departments);
        expect(service.departmentList[2].depId).toEqual(103)
        expect(service.departmentList[2].depName).toBeNull();
        expect(service.departmentList[2].createdBy).toBeNull();

        service.modifyDepartmentList({depId: 103, depName: "IT", createdBy: "Bob"});
        expect(service.departmentList[2].depId).toEqual(103)
        expect(service.departmentList[2].depName).toEqual("IT");
        expect(service.departmentList[2].createdBy).toEqual("Bob");
    });

    it('should return true when number is less then zero', function(){
          expect(service.isLengtsmallerOrEqualToZero( 0 )).toBeTruthy();
          expect(service.isLengtsmallerOrEqualToZero( -1 )).toBeTruthy();
    });

    it('should return false when number is greater then zero', function(){
        expect(service.isLengtsmallerOrEqualToZero( 1 )).toBeFalsy();
        expect(service.isLengtsmallerOrEqualToZero( 9 )).toBeFalsy();
    });
});