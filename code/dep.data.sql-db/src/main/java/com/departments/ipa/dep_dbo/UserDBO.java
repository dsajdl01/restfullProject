package com.departments.ipa.dep_dbo;

import com.departments.ipa.data.StaffTable;
import com.departments.ipa.fault.exception.DepartmentFaultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by david on 13/11/16.
 */
public class UserDBO {

    protected static final Logger LOGGER = LoggerFactory.getLogger(UserDBO.class);
    private Connection con;

    private String LOGIN_USER = "SELECT user_id FROM staff_login WHERE email = ? AND password = ? ";
    private String SQL_STAFF = "SELECT id as userId, dep_id as depId, name, dob, start_day as startDay, last_day as lastDay, position, email, comments "
                                + "FROM department_db.staff WHERE id = ?";

    public UserDBO(Connection con) {
        this.con = con;
    }

    public Integer loninUser(String email, String password) throws DepartmentFaultService {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(LOGIN_USER);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getRow() <= 0 ? null :  resSet.getInt("user_id");
        } catch (SQLException sqlE) {
            LOGGER.error("loninUser: {}", sqlE);
            throw new DepartmentFaultService("Inable to connect to databese");
        }
    }

    public StaffTable getStaffDetails(Integer userId) throws DepartmentFaultService {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(SQL_STAFF);
            preparedStatement.setInt(1, userId);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return null;
            return new StaffTable(res.getInt("userId"), res.getInt("depId"), res.getString("name"), getDate(res.getString("dob")), getDate(res.getString("startDay")),
                    getDate(res.getString("lastDay")),res.getString("position"), res.getString("email"), res.getString("comments") );
        } catch (SQLException sqlE){
            LOGGER.error("loninUser: {}", sqlE);
            throw new DepartmentFaultService("Inable to connect to databese");
        }
    }

    private Date getDate(String date) throws DepartmentFaultService {
        try {
            if(date == null ) return null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");
            Date d = formatter.parse(date);
            return d;
        } catch (ParseException e) {
            LOGGER.error("getDate: format.parse error: " + e);
            throw new DepartmentFaultService( date + " can not be converted into Date");
        }
    }
}
