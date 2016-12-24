package com.departments.dto.dep_dbo;

import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
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
    private CommonConversions commonConv = new CommonConversions();

    private String LOGIN_USER_ID = "SELECT user_id FROM staff_login WHERE email = ?";
    private String CHECK_EMAIL_QUERY = "SELECT count(email) AS numOfMails FROM staff_login WHERE email like ? ";
    private String SATFF_PASSWORD_QUERY = " SELECT password FROM staff_login WHERE email = ?";
    private String ADD_NEW_STAFF = "INSERT INTO staff (dep_id, name, dob, start_day, last_day, position, email, comments) "
                                            + "VALUES (?, ?, ?, ?, null, ?, ?, ?)";
    private String ADD_NEW_STAFF_LOGIN = "INSERT INTO staff_login (user_id, email, password, first_login) "
                                                        + "VALUES (?, ?, ?, 1)";
    private String SQL_STAFF_ID_AND_NAME = "SELECT id as userId, name FROM staff WHERE id = ?";
    private String ALL_SATFF_DETAILS = "select id, dep_id, name, dob, start_day, last_day, position, email, comments from staff where id = ?";

    private String DELETE_STAFF_LOGIN_DETAILS = "DELETE FROM staff_login WHERE user_id = ?";
    private String DELETE_STAFF_DETAILS = "DELETE FROM staff WHERE id = ?";

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
            throw new SQLFaultException("Enable to connect to database");
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
            throw new SQLFaultException("Enable to connect to database");
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
            throw new SQLFaultException("Enable to connect to database");
        }
    }

    public LoginStaff getLogInStaffDetails(Integer userId) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(SQL_STAFF_ID_AND_NAME);
            preparedStatement.setInt(1, userId);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return null;
            return new LoginStaff(res.getInt("userId"), res.getString("name"));
        } catch (SQLException sqlE){
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        }
    }

    public Staff getStaffDetails(int staffId) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(ALL_SATFF_DETAILS);
            preparedStatement.setInt(1, staffId);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return null;
            String value = res.getString("dob");
            Date dob = ( value == null)? null : commonConv.getDateFromString(value);
            value = res.getString("start_day");
            Date startDay = (value == null)? null : commonConv.getDateFromString(value);
            value = res.getString("last_day");
            Date lastDay = (value == null) ? null : commonConv.getDateFromString(value);
            return new Staff(res.getInt("id"), res.getInt("dep_id"), res.getString("name"),dob, startDay, lastDay, res.getString("position"),
                    res.getString("email"), res.getString("comments"));
        } catch (SQLException sqlE){
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        } catch (Exception e) {
            LOGGER.error("getStaffDetails: {}", e);
            throw  new RuntimeException(e.getMessage());
        }
    }

    public void saveNewStaff(Staff staff) throws SQLFaultException {
        LOGGER.info("DTO saveNewStaff: name={}",staff.getName());
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(ADD_NEW_STAFF);
            preparedStatement.setInt(1, staff.getDepId());
            preparedStatement.setString(2, staff.getName());
            preparedStatement.setString(3, getString(staff.getDob()));
            preparedStatement.setString(4, getString(staff.getStartDay()));
            preparedStatement.setString(5, staff.getPosition());
            preparedStatement.setString(6, commonConv.stringIsNullOrEmpty(staff.getEmail()) ? null : staff.getEmail());
            preparedStatement.setString(7, commonConv.stringIsNullOrEmpty(staff.getComment()) ? null : staff.getComment());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlE){
            LOGGER.error("saveNewStaff: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        }

    }

    private String getString(Date date) {
        if (date == null) return null;
        return commonConv.convartDateToString(date);
    }

    private final String GET_HIGHST_ID_SQL = "SELECT max(id) AS staff_id FROM staff";
    public int getNewStaffId() throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(GET_HIGHST_ID_SQL);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return 0;
            return  res.getInt("staff_id");
        } catch (SQLException sqlE){
            LOGGER.error("getNewStaffId: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        }
    }

    public void deleteStaffLoginDetails(int staffId) throws SQLFaultException  {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(DELETE_STAFF_LOGIN_DETAILS);
            preparedStatement.setInt(1, staffId);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlE){
            LOGGER.error("deleteStaffDetails: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database when deleting staff");
        }
    }

    public void deleteStaffDetails(int staffId) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(DELETE_STAFF_DETAILS);
            preparedStatement.setInt(1, staffId);
            preparedStatement.executeUpdate();
        } catch (SQLException sqlE){
            LOGGER.error("deleteStaffDetails: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database when deleting staff");
        }
    }

    public void  saveLoginDetails(LoginDetails loginDetail, int staffId) throws  SQLFaultException {
        LOGGER.info("DTO saveLoginDetail: staffId={}, mail={}", loginDetail.getEmail(), staffId );
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(ADD_NEW_STAFF_LOGIN);
            preparedStatement.setInt(1, staffId);
            preparedStatement.setString(2, loginDetail.getEmail());
            preparedStatement.setString(3, loginDetail.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlE){
            LOGGER.error("getNewStaffId: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        }
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
