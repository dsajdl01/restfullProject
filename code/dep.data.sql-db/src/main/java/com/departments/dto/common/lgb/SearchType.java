package com.departments.dto.common.lgb;

import com.departments.dto.fault.exception.ValidationException;

/**
 * Created by david on 26/12/16.
 */
public enum SearchType {

    NAME, DOB;

    public static SearchType fromString(String value) throws  ValidationException {

        if ("name".equalsIgnoreCase(value)) {
            return NAME;
        } else if ("dob".equalsIgnoreCase(value)) {
            return DOB;
        } else {
            throw new ValidationException("Invalid search type: " + value);
        }
    }
}
