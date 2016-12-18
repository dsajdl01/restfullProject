package com.departments.ipa.fault.exception;

/**
 * Created by david on 18/12/16.
 */
public class ValidationException  extends Exception {

    public ValidationException(){
        super();
    }

    public ValidationException(String message){
        super(message);
    }
}
