package com.department.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by david on 10/08/16.
 */
public class DepartmentConfigLoader implements ServletContextListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentConfigLoader.class);

    public DepartmentConfigLoader() {
        LOGGER.info("DepartmentConfigLoader() = constructor={}", DepartmentConfigLoader.class.toString());
        this.printThemAll();
    }

    private void printThemAll() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            String filename = "configDep.properties";
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                LOGGER.info("DepartmentConfigLoader Sorry, unable to find " + filename);
//                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            prop.load(input);

            Enumeration<?> e = prop.propertyNames();
            String data = "\nProperties values:\n";
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                String value = prop.getProperty(key);
                data +=  key + " " + value + "\n";
            }
            LOGGER.info(data);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOGGER.info("DepartmentConfigLoader() = contextInitialized() method: {}", DepartmentConfigLoader.class.toString());
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
