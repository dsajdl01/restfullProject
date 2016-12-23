package com.departments.dto.data;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by david on 28/08/16.
 */
public class DepartmentTableTest {

    private DepartmentTable dep;

    @Before
    public void setUp(){
        dep = new DepartmentTable(1, "IT Department", 2);
    }

    @Test
    public void testGetId(){
        assertThat(dep.getId(), is(1));
    }

    @Test
    public void testGetGetName(){
        assertThat(dep.getName(), is("IT Department"));
    }

    @Test
    public void testGetCreaterID(){
        assertThat(dep.getCreatedBy(), is(2));
    }

    @Test
    public void testEqualsTrue(){
        assertThat(dep.equals(dep), is(true));
    }

    @Test
    public void testEqualsFalse(){
        assertThat(dep.equals(new DepartmentTable(2, "IT Department", 1)), is(false));
    }
}
