package com.department.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david on 06/08/16.
 */
public class AbstrctDepartmentProperties{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstrctDepartmentProperties.class);

    private static final String CONFIG_FILE = "department.properties";
    public AbstrctDepartmentProperties(){
        LOGGER.info("in AbstrctDepartmentProperties CLASS: {}", AbstrctDepartmentProperties.class);
    }


}
