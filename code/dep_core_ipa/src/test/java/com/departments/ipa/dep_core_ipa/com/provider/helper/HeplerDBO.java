package com.departments.ipa.dep_core_ipa.com.provider.helper;

import com.departments.ipa.data.PropertiesDataConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by david on 19/08/16.
 */
public class HeplerDBO {

    private final String DB_PROPERTIES_CLASS_NAME = "db.className";
    private final String DB_PROPERTIES_USER = "db.user";
    private final String DB_PROPERTIES_PASSWORD = "db.password";
    private final String DB_PROPERTIES_CONNECTION = "db.connectionTest";

    private static final Logger LOGGER = LoggerFactory.getLogger( HeplerDBO.class);
    private static PropertiesDataConfig propertiesDataTestConfig;

    public HeplerDBO (){
        System.out.println("HeplerDBO ()");
        propertiesDataTestConfig = new LoaderPropertyTest().loadTestPropertiers(new PropertiesDataConfig());
        setPropertyForSqlConnection();
    }

    private void setPropertyForSqlConnection() {
        propertiesDataTestConfig.addPropertiesDataConfig("db.connection", propertiesDataTestConfig.getValue(DB_PROPERTIES_CONNECTION));
    }

    public Connection getDbTestConnection() {

        //Statement statement = null;

        Connection con = null;
        try {
            if(this.propertiesDataTestConfig == null){
                LOGGER.error("Unable to connect to database. propertiesData={}", this.propertiesDataTestConfig);
                return null;
            }
            Class.forName(propertiesDataTestConfig.getValue(DB_PROPERTIES_CLASS_NAME));

            Properties p = new Properties();
            p.put("user", propertiesDataTestConfig.getValue(DB_PROPERTIES_USER));
            p.put("password", propertiesDataTestConfig.getValue(DB_PROPERTIES_PASSWORD));

            // Now to connect
            con = DriverManager.getConnection(propertiesDataTestConfig.getValue(DB_PROPERTIES_CONNECTION), p);
        }
        catch (ClassNotFoundException e){
            LOGGER.error("DepartmentDBOConnection.class. Class not found exception={} ", e);
        }
        catch (SQLException e) {
            LOGGER.error("DepartmentDBOConnection.class. Unable to connect to database={} ", e);
        }

        return con;
    }

    public PropertiesDataConfig getPropertiesTestDataConfig(){
        return propertiesDataTestConfig;
    }

}
