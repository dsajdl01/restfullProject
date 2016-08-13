package com.department.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david on 06/08/16.
 */
public class DepartmentWebProperties extends AbstrctDepartmentProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentWebProperties.class);
    public DepartmentWebProperties(){
        super();
        LOGGER.info("in DepartmentWebProperties CLASS: {}", DepartmentWebProperties.class);
    }

}
