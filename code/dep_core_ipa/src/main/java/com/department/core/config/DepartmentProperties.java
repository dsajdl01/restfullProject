package com.department.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by david on 06/08/16.
 */
public class DepartmentProperties  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentProperties.class);

    public DepartmentProperties(){
        super();
        LOGGER.info("in DepartmentProperties CLASS: {}", DepartmentProperties.class);
    }
}
