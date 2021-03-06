package com.departments.dto.common.lgb;

import com.departments.dto.fault.exception.ValueConversionFaultException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by david on 26/08/16.
 */
public class CommonConversions {

    public String convertDateToString(Date date){
        if(date == null) return null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public Date getDateFromString(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        return dateFormat.parse(date);
    }

    public boolean isStringDigit(String value){
        return value.matches("[0-9]+");
    }

    public Integer convertStringToInteger(String value) throws ValueConversionFaultException {
        if (isStringDigit(value)) {
            return Integer.valueOf(value);
        } else {
           throw new ValueConversionFaultException("String must contains only digits, " + value);
        }
    }

    public boolean hasStringMaxLength(String val, int length){
        return  val.length() >= length;
    }

    public boolean stringIsNullOrEmpty(String val) {
        return val == null || val.trim().length() == 0|| (val.equalsIgnoreCase("null"));
    }

    public boolean doesEmailIsValid(String email) {
        Pattern regex = Pattern.compile("^[_a-z0-9-\\+\"]+(\\.[_a-z0-9-\\+\"]+)*@(?!-)[a-z0-9-]+(\\.[a-z0-9-]+)+$", Pattern.CASE_INSENSITIVE);
        return regex.matcher(email).matches();
    }
}