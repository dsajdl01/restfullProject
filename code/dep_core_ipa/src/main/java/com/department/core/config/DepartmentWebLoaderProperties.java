package com.department.core.config;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Created by david on 06/08/16.
 */

@Provider
public class DepartmentWebLoaderProperties implements Factory<DepartmentWebProperties>{

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentWebLoaderProperties.class);
    private static class LazyHolder {
        static DepartmentWebProperties instance;
        static {
            try {
                LOGGER.debug("Initialising DepartmentWebProperties");
                instance = new DepartmentWebProperties();
            } catch (Exception e) {
                LOGGER.error("Could not create an instance of DepartmentWebProperties: {}", e.getMessage());
            }
        }
    }

    public DepartmentWebProperties provide() {
        return LazyHolder.instance;
    }

    public void dispose(DepartmentWebProperties departmentWebProperties) {
    }
}
