package com.departments.dto.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by david on 28/08/16.
 */
public class DepartmentTest {

    private Department dep;

    @Before
    public void setUp()
    {
        dep = new Department(1, "IT Department", "Fred Marly");
    }

    @After
    public void After(){
        dep = null;
    }

    @Test
    public void testGetId(){
        assertThat(dep.getId(), is(1));
    }

    @Test
    public void testGetDepartmentName(){
        assertThat(dep.getName(), is("IT Department"));
    }

    @Test
    public void testGetCreater(){
        assertThat(dep.getCreater(), is("Fred Marly"));
    }

    @Test
    public void testEqualsTrue(){
        assertThat(dep.equals(dep), is(true));
    }

    @Test
    public void testEqualsFalse(){
        assertThat(dep.equals(new Department(2, "IT Department", "Fred Marly")), is(false));
    }
}
