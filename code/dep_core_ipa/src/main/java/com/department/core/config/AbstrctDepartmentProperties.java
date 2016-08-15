package com.department.core.config;

import com.departments.ipa.data.PropertiesDataConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by david on 06/08/16.
 */
public class AbstrctDepartmentProperties{

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstrctDepartmentProperties.class);

    protected static final String CONFIG_FILE = "configDep.properties";
    public AbstrctDepartmentProperties(){
        LOGGER.info("in AbstrctDepartmentProperties CLASS: {}", AbstrctDepartmentProperties.class);
    }
    public void loader(){}

}
