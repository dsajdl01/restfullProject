package com.departments.ipa.dep_core_ipa.com.provider.helper;

import com.department.core.config.DepartmentProperties;
import com.departments.ipa.data.PropertiesDataConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by david on 22/08/16.
 */
public class LoaderPropertyTest {

    protected LoaderPropertyTest(){
    //    loadTestPropertiers(new PropertiesDataConfig());
    }

    protected static final Logger LOGGER = LoggerFactory.getLogger(LoaderPropertyTest.class);
    private static final String CONFIG_FILE = "configDep.properties";

    public PropertiesDataConfig loadTestPropertiers(PropertiesDataConfig propertiesDataTestConfig) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = CONFIG_FILE;
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                LOGGER.info("DepartmentConfigLoader Sorry, unable to find " + filename);
                return propertiesDataTestConfig;
            }
            prop.load(input);
            Enumeration<?> e = prop.propertyNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                propertiesDataTestConfig.addPropertiesDataConfig(key, value);
                System.out.println(key + ": " + value);
            }

        } catch (IOException ex) {
            LOGGER.error("DepartmentConfigLoader error={}", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("DepartmentConfigLoader unable to close input={}", e);
                }
            }
        }
        return propertiesDataTestConfig;
    }
}
