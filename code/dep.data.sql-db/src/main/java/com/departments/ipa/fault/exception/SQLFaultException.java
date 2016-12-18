package com.departments.ipa.fault.exception;

/**
 * Created by david on 14/08/16.
 */
public class SQLFaultException extends Exception {

    public SQLFaultException(){
        super();
    }

    public SQLFaultException(String message){
        super(message);
    }
}
