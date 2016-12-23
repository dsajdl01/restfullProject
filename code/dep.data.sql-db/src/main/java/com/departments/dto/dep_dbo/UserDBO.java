package com.departments.dto.dep_dbo;

import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.StaffTable;
import com.departments.dto.fault.exception.SQLFaultException;
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

    private String LOGIN_USER_ID = "SELECT user_id FROM staff_login WHERE email = ?";
    private String CHECK_EMAIL_QUERY = "SELECT count(email) AS numOfMails FROM staff_login WHERE email like ? ";
    private String SATFF_PASSWORD_QUERY = " SELECT password FROM staff_login WHERE email = ?";

    private String SQL_STAFF = "SELECT id as userId, name "
                                + "FROM staff WHERE id = ?";

    public UserDBO(Connection con) {
        this.con = con;
    }

    public Integer loninUserId(String email) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(LOGIN_USER_ID);
            preparedStatement.setString(1, email);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getRow() <= 0 ? null :  resSet.getInt("user_id");
        } catch (SQLException sqlE) {
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Inable to connect to databese");
        }
    }

    public String getLoginStaffPassword(String email) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(SATFF_PASSWORD_QUERY);
            preparedStatement.setString(1, email);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getRow() <= 0 ? null :  resSet.getString("password");
        } catch (SQLException sqlE) {
            LOGGER.error("getLoginStaffPassword: {}", sqlE);
            throw new SQLFaultException("Inable to connect to databese");
        }
    }

    public boolean doesEmailExist(String email) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(CHECK_EMAIL_QUERY);
            preparedStatement.setString(1, email);
            ResultSet resSet = preparedStatement.executeQuery();
            resSet.next();
            return resSet.getInt("numOfMails") == 0 ? false : true;
        } catch (SQLException sqlE) {
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Inable to connect to databese");
        }
    }

    public LoginStaff getStaffDetails(Integer userId) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(SQL_STAFF);
            preparedStatement.setInt(1, userId);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return null;
            return new LoginStaff(res.getInt("userId"), res.getString("name"));
        } catch (SQLException sqlE){
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Inable to connect to databese");
        }
    }

    public void saveNewStaff(StaffTable staff) throws SQLFaultException {
        LOGGER.info("DTO saveNewStaff: name={}",staff.getName());
    }

    public void  saveLoginDetails(LoginDetails loginDetail) throws  SQLFaultException {
        LOGGER.info("DTO saveLoginDetail: mail={}, password={}", loginDetail.getEmail(), loginDetail.getPassword());
    }

    private Date getDate(String date) throws SQLFaultException {
        try {
            if(date == null ) return null;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-mm");
            Date d = formatter.parse(date);
            return d;
        } catch (ParseException e) {
            LOGGER.error("getDate: format.parse error: " + e);
            throw new SQLFaultException( date + " can not be converted into Date");
        }
    }
}