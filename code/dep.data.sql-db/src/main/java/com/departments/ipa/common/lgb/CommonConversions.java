package com.departments.ipa.common.lgb;

import com.departments.ipa.fault.exception.DepartmentValueConversionFault;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david on 26/08/16.
 */
public class CommonConversions {

    public String convartDateToString(Date date){
        if(date == null) return null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public Date getDateFromString(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(date);
    }

    public boolean isStringDigit(String value){
        return value.matches("[0-9]+");
    }

    public Integer convertStringToInteger(String value) throws  DepartmentValueConversionFault {
        if (isStringDigit(value)) {
            return Integer.valueOf(value);
        } else {
           throw new DepartmentValueConversionFault("String must contains only digits, " + value);
        }
    }

    public boolean hasStringValue(String val) {
        return val == null || val.trim().length() == 0|| (val.equalsIgnoreCase("null"));
    }
}