package com.departments.ipa.dep_dbo;

import com.departments.ipa.data.PropertiesDataConfig;
import com.departments.ipa.fault.exception.DepartmentDatabaseFaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by david on 13/08/16.
 */
public class DepartmentDBOConnection {

    private final String DB_PROPERTIES_CLASS_NAME = "db.className";
    private final String DB_PROPERTIES_USER = "db.user";
    private final String DB_PROPERTIES_PASSWORD = "db.password";
    private final String DB_PROPERTIES_CONNECTION = "db.connection";
    private PropertiesDataConfig propertiesData;

    private static final Logger LOGGER = LoggerFactory.getLogger( DepartmentDBOConnection.class);

    public DepartmentDBOConnection (PropertiesDataConfig propertiesData){
        this.propertiesData = propertiesData;
        LOGGER.info("DepartmentDBOConnection class constractor");
    }

    public Statement getDbConnection() {

        Statement statement = null;
        try {
            if(this.propertiesData == null){
                LOGGER.error("Unable to connect to database. propertiesData={}", this.propertiesData);
                return null;
            }
            Class.forName(propertiesData.getValue(DB_PROPERTIES_CLASS_NAME));

            Properties p = new Properties();
            p.put("user", propertiesData.getValue(DB_PROPERTIES_USER));
            p.put("password", propertiesData.getValue(DB_PROPERTIES_PASSWORD));

            // Now to connect
            Connection c = DriverManager.getConnection(propertiesData.getValue(DB_PROPERTIES_CONNECTION), p);
            statement = c.createStatement();
        }
        catch (ClassNotFoundException e){
            LOGGER.error("DepartmentDBOConnection.class. Class not found exception={} ", e);
        }
        catch (SQLException e) {
            LOGGER.error("DepartmentDBOConnection.class. Unable to connect to database={} ", e);
        }

        return statement;
    }
}
