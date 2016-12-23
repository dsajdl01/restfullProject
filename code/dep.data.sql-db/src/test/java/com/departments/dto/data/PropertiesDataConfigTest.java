package com.departments.dto.data;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by david on 28/08/16.
 */
public class PropertiesDataConfigTest {

    private PropertiesDataConfig propeDataConf;

    @Before
    public void setUp(){
        propeDataConf = new PropertiesDataConfig();
    }

    @Test
    public void testAddAndGetValue(){
        String value = "jdbc:mysql://127.0.0.1/department_db";
        String key = "db.connection";
        assertNull(propeDataConf.getValue(key));

        propeDataConf.addPropertiesDataConfig(key, value);
        assertThat(propeDataConf.getValue(key), is(value));
    }
}
