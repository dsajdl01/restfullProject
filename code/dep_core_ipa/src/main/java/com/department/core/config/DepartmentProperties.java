package com.department.core.config;

import com.departments.dto.data.PropertiesDataConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by david on 06/08/16.
 */
public class DepartmentProperties  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentProperties.class);
    static PropertiesDataConfig propertiesDataConfig = new PropertiesDataConfig();


    public DepartmentProperties(){
        super();
        LOGGER.info("DepartmentProperties CLASS: {}", DepartmentProperties.class);
    }

    public void loadPropeties(Enumeration<?> e, Properties prop){

        System.out.println("\nPROPERTIES VALUES:");
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String value = prop.getProperty(key);
            propertiesDataConfig.addPropertiesDataConfig(key, value);
            System.out.println(key + ": " + value);
        }
        System.out.println("\nProperties loaded successfully ...");
     }

    public PropertiesDataConfig getPropertiesDataConfig(){
        return propertiesDataConfig;
    }
}
