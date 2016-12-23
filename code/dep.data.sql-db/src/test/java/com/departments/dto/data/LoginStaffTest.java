package com.departments.dto.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by david on 21/11/16.
 */
public class LoginStaffTest {

    private LoginStaff loginStaff;
    @Before
    public void setUp()
    {
        loginStaff = new LoginStaff(1, "Fred Marley");
    }

    @After
    public void After(){
        loginStaff = null;
    }

    @Test
    public void getUserIdTest() {
        assertThat(loginStaff.getUserId(), is(1));
    }

    @Test
    public void getNameTest() {
        assertThat(loginStaff.getName(), is("Fred Marley"));
    }
}
