package com.departments.dto.common.lgb;

import com.departments.dto.fault.exception.ValueConversionFaultException;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by David Sajdl on 18/09/16.
 */
public class CommonConversionsTest {

    private CommonConversions commConversions;

    @Before
    public void setUp()
    {
        commConversions = new CommonConversions();
    }

    @Test
    public void convartDateToStringTest() throws ParseException {
        String stringDate = "2016-09-18";

        Date dateFromString = commConversions.getDateFromString(stringDate);
        assertThat(commConversions.convertDateToString(dateFromString), is(stringDate));
    }

    @Test
    public void getDateFromStringTest() throws ParseException  {

        String stringDate = "2016-09-17";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date result = dateFormat.parse(stringDate);

        assertThat(commConversions.getDateFromString(stringDate) , is(result));
    }

    @Test
    public void getDateFromStringError_Test() {
        try {
            commConversions.getDateFromString("2017-08-39");// invalid year
            fail( "getDateFromStringError_Test: should not be thrown ValidationException: ");
        } catch ( ParseException e ) {
            assertThat(e.getMessage(), is("Unparseable date: \"2017-08-39\""));
        }
    }

    @Test
    public void getDateFromStringErrorII_Test() {
        try {
            commConversions.getDateFromString("2017-02-29");// not leap year
            fail( "getDateFromStringError_Test: should not be thrown ValidationException: ");
        } catch ( ParseException e ) {
            assertThat(e.getMessage(), is("Unparseable date: \"2017-02-29\""));
        }
    }

    @Test
    public void isStringDigitFalseTest(){
        assertThat(commConversions.isStringDigit("ff"), is(false));
        assertThat(commConversions.isStringDigit("1t2"), is(false));
        assertThat(commConversions.isStringDigit("3p0"), is(false));
        assertThat(commConversions.isStringDigit("333%"), is(false));
    }

    @Test
    public void isStringDigitTrueTest(){
        assertThat(commConversions.isStringDigit("91"), is(true));
        assertThat(commConversions.isStringDigit("906571"), is(true));
        assertThat(commConversions.isStringDigit("57614520092"), is(true));
    }

    @Test
    public void concertStringToIntegerTest() throws ValueConversionFaultException {
        assertThat(commConversions.convertStringToInteger("11"), is(11));
        assertThat(commConversions.convertStringToInteger("9742"), is(9742));
        assertThat(commConversions.convertStringToInteger("75"), is(75));
    }

    @Test
    public void concertStringToIntegerErrorTest() {
        try {
            commConversions.convertStringToInteger("1-Y-1");
        } catch (ValueConversionFaultException d) {
            assertThat(d.getMessage(), is ("String must contains only digits, 1-Y-1" ));
        }
    }

    @Test
    public void stringIsNullOrEmptyTrue_Test(){
        assertThat(commConversions.stringIsNullOrEmpty(null), is(true));
        assertThat(commConversions.stringIsNullOrEmpty(""), is(true));
        assertThat(commConversions.stringIsNullOrEmpty("null"), is(true));
        assertThat(commConversions.stringIsNullOrEmpty("NULL"), is(true));
        assertThat(commConversions.stringIsNullOrEmpty("NuLl"), is(true));
    }

    @Test
    public void stringIsNullOrEmptyFalse_Test(){
        assertThat(commConversions.stringIsNullOrEmpty("value"), is(false));
        assertThat(commConversions.stringIsNullOrEmpty("David"), is(false));
        assertThat(commConversions.stringIsNullOrEmpty("Java"), is(false));
    }

    @Test
    public void doesEmailIsValidationTrue_Test() {
        assertThat(commConversions.doesEmailIsValid("ds@co.uk"), is(true));
        assertThat(commConversions.doesEmailIsValid("someEmail@Example.com"), is(true));
        assertThat(commConversions.doesEmailIsValid("12mails@example.com"), is(true));
        assertThat(commConversions.doesEmailIsValid("david.smith@co.uk"), is(true));
    }

    @Test
    public void doesEmailIsValidationFalse_Test() {
        assertThat(commConversions.doesEmailIsValid("ds@co@uk"), is(false));
        assertThat(commConversions.doesEmailIsValid("david.uk"), is(false));
        assertThat(commConversions.doesEmailIsValid("@co.uk"), is(false));
        assertThat(commConversions.doesEmailIsValid("dsmith.co@"), is(false));
    }

    @Test
    public void hasStringMaxLengthTrue_Test() {
        assertThat(commConversions.hasStringMaxLength("david", 5), is(true));
        assertThat(commConversions.hasStringMaxLength("hello", 5), is(true));
        assertThat(commConversions.hasStringMaxLength("helloworld", 10), is(true));
        assertThat(commConversions.hasStringMaxLength("smithAlex", 7), is(true));
        assertThat(commConversions.hasStringMaxLength("fred", 4), is(true));
    }

    @Test
    public void hasStringMaxLengthFalse_Test() {
        assertThat(commConversions.hasStringMaxLength("david", 8), is(false));
        assertThat(commConversions.hasStringMaxLength("hello", 9), is(false));
        assertThat(commConversions.hasStringMaxLength("helloworld", 20), is(false));
        assertThat(commConversions.hasStringMaxLength("smithAlex", 10), is(false));
        assertThat(commConversions.hasStringMaxLength("fred", 5), is(false));
    }
}
