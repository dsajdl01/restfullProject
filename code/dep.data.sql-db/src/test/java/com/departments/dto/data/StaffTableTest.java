package com.departments.dto.data;

import com.departments.dto.common.lgb.CommonConversions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

import java.text.ParseException;

/**
 * Created by david on 28/08/16.
 */
public class StaffTableTest {

    private StaffTable staff;
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffTableTest.class);
    private CommonConversions convertion;

    @Before
    public void setUp(){
        try {
            convertion = new CommonConversions();
            staff = new StaffTable(1, 1, "Fred Marly", convertion.getDateFromString("1998-02-03"), convertion.getDateFromString("2011-07-26"), null, "Fron End", "fm@example.com", "Good also in design");
        } catch (ParseException e){
            LOGGER.error("StaffTableTest class, data convertion error: {}", e);
        }
    }

    @Test
    public void testGetId(){
        assertThat(staff.getId(), is(1));
    }


    @Test
    public void testGetDepId(){
        assertThat(staff.getDepId(), is(1));
    }

    @Test
    public void testName(){
        assertThat(staff.getName(),is("Fred Marly"));
    }

    @Test
    public void testBOD() throws ParseException {
        assertThat(staff.getDob(), is(convertion.getDateFromString("1998-02-03")));
    }

    @Test
    public void testStartDay() throws ParseException {
        assertThat(staff.getStartDay(), is(convertion.getDateFromString("2011-07-26")));
    }

    @Test
    public void testLastDay(){
        assertNull(staff.getLastDay());
    }

    @Test
    public void testGetPosition(){
        assertThat(staff.getPosition(), is("Fron End"));
    }

    @Test
    public void testGetEmail(){
        assertThat(staff.getEmail(), is("fm@example.com"));
    }

    @Test
    public void testGetComments(){
        assertThat(staff.getComment(), is("Good also in design"));
    }
}
