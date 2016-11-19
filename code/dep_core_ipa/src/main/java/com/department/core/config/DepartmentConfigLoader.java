package com.department.core.config;

import com.departments.ipa.data.PropertiesDataConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by david on 10/08/16.
 */
public class DepartmentConfigLoader extends AbstrctDepartmentProperties implements ServletContextListener{

    static PropertiesDataConfig propertiesDataConfig = new PropertiesDataConfig();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        loader();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void loader() {

        printLoading();
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = CONFIG_FILE;
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                LOGGER.info("DepartmentConfigLoader Sorry, unable to find " + filename);
                return;
            }
            prop.load(input);
            new DepartmentProperties().loadPropeties(prop.propertyNames(), prop);

        } catch (IOException ex) {
            LOGGER.error("DepartmentConfigLoader error={}", ex);
            return;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.error("DepartmentConfigLoader unable to close input={}", e);
                    return;
                }
            }
        }

    }

    private void printLoading(){
        String prefix = "[INFO] ";
        System.out.println(prefix + "  _ _ _ _                     __       ");
        System.out.println(prefix + "   |     \\                   / _\\    ");
        System.out.println(prefix + "   |  _   \\  _ _ _  _ _ _   / /     _ _  _  __   _ _ _");
        System.out.println(prefix + "   | | \\   |/  _ _|/   _ \\ | |     / _ \\| |/  \\ /  _ _|");
        System.out.println(prefix + "   | |  )  |  |_  |   |_\\ || |    | / \\ |  /-^-|  |_");
        System.out.println(prefix + "   | |_/   |   _| |    _ / | |  _ | | | |  |   |   _|");
        System.out.println(prefix + "   |      /|  |_ _|   |     \\ \\/ /| \\_/ |  |   |  |_ _ _");
        System.out.println(prefix + "  _|_ _ _/ | _ _ _| _ |_[]__ \\__/__\\_ _/|_ |_ _|_ _ _ __\\" + " is launching ...\n");

    }
}
