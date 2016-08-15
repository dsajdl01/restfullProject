package com.departments.ipa.fault.exception;

/**
 * Created by david on 14/08/16.
 */
public class DepartmentFaultService extends Exception {

    public DepartmentFaultService(){
        super();
    }

    public DepartmentFaultService(String message){
        super(message);
    }
}
