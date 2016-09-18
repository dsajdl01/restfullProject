package com.departments.ipa.common.lgb;

import com.departments.ipa.fault.exception.DepartmentFaultService;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
        assertThat(commConversions.convartDateToString(dateFromString), is(stringDate));
    }

    @Test
    public void getDateFromStringTest() throws ParseException  {

        String stringDate = "2016-09-17";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date result = dateFormat.parse(stringDate);

        assertThat(commConversions.getDateFromString(stringDate) , is(result));
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
    public void concertStringToIntegerTest() throws DepartmentFaultService {
        assertThat(commConversions.concertStringToInteger("11"), is(11));
        assertThat(commConversions.concertStringToInteger("9742"), is(9742));
        assertThat(commConversions.concertStringToInteger("75"), is(75));
    }

    @Test
    public void concertStringToIntegerErrorTest() {
        try {
            commConversions.concertStringToInteger("1-Y-1");
        } catch (DepartmentFaultService d) {
            assertThat(d.getMessage(), is ("String must contains only digits, 1-Y-1" ));
        }
    }

    @Test
    public void hasStringValueTestTrue(){
        assertThat(commConversions.hasStringValue(null), is(true));
        assertThat(commConversions.hasStringValue(""), is(true));
        assertThat(commConversions.hasStringValue("null"), is(true));
    }

    @Test
    public void hasStringValueTestFalse(){
        assertThat(commConversions.hasStringValue("value"), is(false));
        assertThat(commConversions.hasStringValue("David"), is(false));
        assertThat(commConversions.hasStringValue("Java"), is(false));
    }
}
