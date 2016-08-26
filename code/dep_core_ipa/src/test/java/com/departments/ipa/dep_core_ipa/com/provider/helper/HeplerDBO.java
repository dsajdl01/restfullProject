package com.departments.ipa.dep_core_ipa.com.provider.helper;

import com.departments.ipa.common.lgb.CommonConversions;
import com.departments.ipa.data.DepartmentTable;
import com.departments.ipa.data.PropertiesDataConfig;
import com.departments.ipa.data.StaffTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by david on 19/08/16.
 */
public class HeplerDBO {

    private final String DB_PROPERTIES_CLASS_NAME = "db.className";
    private final String DB_PROPERTIES_USER = "db.user";
    private final String DB_PROPERTIES_PASSWORD = "db.password";

    private final String DB_PROPERTIES_CONNECTION = "db.connectionTest";

    private final String SQL_TRUNCATE_DETARTMENT_TABLE = "truncate table department";
    private final String SQL_TRUNCATE_STAFF_TABLE = "truncate table staff";
    private final String SQL_REMOVE_STAFF_CONSTRAINT = "ALTER TABLE staff DROP FOREIGN KEY db_04E4BC85_staff_id";
    private final String SQL_ADD_STAFF_CONSTRAINT = "ALTER TABLE staff ADD CONSTRAINT db_04E4BC85_staff_id FOREIGN KEY (dep_id) REFERENCES department (id)";

    private static final Logger LOGGER = LoggerFactory.getLogger( HeplerDBO.class);
    private static PropertiesDataConfig propertiesDataTestConfig;
    private Connection con;
    private CommonConversions convertion;


    private static final String SQL_QUARY_ADD_DEPARTMENT =  "INSERT INTO department(name, createdBy) VALUES(?,?)";
    private static final String SQL_QUARY_ADD_STAFF = "INSERT INTO staff (dep_id, name, dob, start_day, last_day, position, email, comments) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    public HeplerDBO (){
        System.out.println("HeplerDBO ()");
        convertion = new CommonConversions();
        propertiesDataTestConfig = new LoaderPropertyTest().loadTestPropertiers(new PropertiesDataConfig());
        setPropertyForSqlConnection();
    }

    private void setPropertyForSqlConnection() {
        propertiesDataTestConfig.addPropertiesDataConfig("db.connection", propertiesDataTestConfig.getValue(DB_PROPERTIES_CONNECTION));
    }

    public Connection getDbTestConnection() {
        con = null;
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
        catch (ClassNotFoundException e) {
            LOGGER.error("DepartmentDBOConnection.class. Class not found exception={} ", e);
        }
        catch (SQLException e) {
            LOGGER.error("DepartmentDBOConnection.class. Unable to connect to database={} ", e);
        }
        return con;
    }

    public void addDepartmentValuesToTable(List<DepartmentTable> department) throws SQLException {
            PreparedStatement preparedStatement = con.prepareStatement(SQL_QUARY_ADD_DEPARTMENT);
            for (DepartmentTable dep : department) {
                preparedStatement.setString(1, dep.getName());
                preparedStatement.setInt(2, dep.getCreatedBy());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
    }

    public void addStaffValuesToTable(List<StaffTable> staffTables) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(SQL_QUARY_ADD_STAFF);
        for (StaffTable staff : staffTables) {
            preparedStatement.setInt(1, staff.getDepId());
            preparedStatement.setString(2, staff.getName());
            preparedStatement.setString(3, convertion.convartDateToString(staff.getDob()));
            preparedStatement.setString(4, convertion.convartDateToString(staff.getStartDay()));
            preparedStatement.setString(5, convertion.convartDateToString(staff.getLastDay()));
            preparedStatement.setString(6, staff.getPosition());
            preparedStatement.setString(7, staff.getEmail());
            preparedStatement.setString(8, staff.getComment());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    public void truncateTable() throws SQLException{

            PreparedStatement preparedStatement = con.prepareStatement(SQL_REMOVE_STAFF_CONSTRAINT);
            preparedStatement.executeUpdate();

            preparedStatement = con.prepareStatement(SQL_TRUNCATE_STAFF_TABLE);
            preparedStatement.executeUpdate();

            preparedStatement = con.prepareStatement(SQL_TRUNCATE_DETARTMENT_TABLE);
            preparedStatement.executeUpdate();

            preparedStatement = con.prepareStatement(SQL_ADD_STAFF_CONSTRAINT);
            preparedStatement.executeUpdate();
    }

    public PropertiesDataConfig getPropertiesTestDataConfig(){
        return propertiesDataTestConfig;
    }
}
