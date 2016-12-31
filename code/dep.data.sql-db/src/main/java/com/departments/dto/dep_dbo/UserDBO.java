package com.departments.dto.dep_dbo;

import com.departments.dto.common.lgb.CommonConversions;
import com.departments.dto.data.LoginDetails;
import com.departments.dto.data.LoginStaff;
import com.departments.dto.data.Staff;
import com.departments.dto.fault.exception.SQLFaultException;
import com.departments.dto.fault.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String SQL_LOGIN_STAFF_ID_AND_NAME = "SELECT s.id AS userId, s.name, l.first_login FROM staff AS s join staff_login AS l ON s.id = l.user_id WHERE s.id = ?";
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
            preparedStatement = con.prepareStatement(SQL_LOGIN_STAFF_ID_AND_NAME);
            preparedStatement.setInt(1, userId);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return null;
            return new LoginStaff(res.getInt("userId"), res.getString("name"), isFirstLogin(res.getInt("first_login")));
        } catch (SQLException sqlE){
            LOGGER.error("loninUserId: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database");
        }
    }

    public boolean isFirstLogin(int firstLogin) {
        return firstLogin == 1;
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
        return commonConv.convertDateToString(date);
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

    private final String SEARCH_FOR_STAFF_BY_NAME = "SELECT id, dep_id, name, dob, start_day, last_day, position, email, comments FROM staff WHERE dep_id = ? AND name like ?";
    public List<Staff> searchForStaffsByName(int depId, String value) throws  SQLFaultException, ValidationException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(SEARCH_FOR_STAFF_BY_NAME);
            preparedStatement.setInt(1, depId);
            preparedStatement.setString(2, "%" +  value + "%");
            ResultSet res = preparedStatement.executeQuery();
            return processResultStaffProvider(res);
        } catch (SQLException sqlE){
            LOGGER.error("searchForStaffsByName: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database when searching for staff by name.");
        }
    }

    private final String STAFF_ALL_VALUES = "SELECT id, dep_id, name, dob, start_day, last_day, position, email, comments FROM staff";

    private final String SEARCH_FOR_STAFF_BY_DOB =  STAFF_ALL_VALUES + " WHERE dep_id= ? AND dob = ?";
    public List<Staff> searchForStaffsByDOB(int depId, String value) throws  SQLFaultException, ValidationException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(SEARCH_FOR_STAFF_BY_DOB);
            preparedStatement.setInt(1, depId);
            preparedStatement.setString(2, value);
            ResultSet res = preparedStatement.executeQuery();
            return processResultStaffProvider(res);
        } catch (SQLException sqlE){
            LOGGER.error("searchForStaffsByDOB: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database when searching for staff by DOB.");
        }
    }

    private final String UPDATE_STAFF_VALUE = "UPDATE staff SET name=?, dob=?, start_day=?, last_day=?, "
                                                        + "position=?, email=?, comments=? WHERE id = ? AND dep_id = ?";

    public void modifyStaff(Staff staff) throws SQLFaultException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(UPDATE_STAFF_VALUE);
            preparedStatement.setString(1, staff.getName());
            preparedStatement.setString(2, getString(staff.getDob()));
            preparedStatement.setString(3, getString(staff.getStartDay()));
            preparedStatement.setString(4, getString(staff.getLastDay()));
            preparedStatement.setString(5, staff.getPosition());
            preparedStatement.setString(6, commonConv.stringIsNullOrEmpty(staff.getEmail()) ? null : staff.getEmail());
            preparedStatement.setString(7, commonConv.stringIsNullOrEmpty(staff.getEmail()) ? null : staff.getComment());
            preparedStatement.setInt(8, staff.getId());
            preparedStatement.setInt(9, staff.getDepId());
            preparedStatement.executeUpdate();
        } catch (SQLException sqlE){
            LOGGER.error("searchForStaffsByDOB: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database when searching for staff by DOB.");
        }
    }

    private final String STAFF_BY_STAFFID_SQL = "SELECT " + STAFF_ALL_VALUES + " FROM staff WHERE id= ? ";

    public Staff checkIfStaffExist(int staffId) throws SQLFaultException, ValidationException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement(STAFF_BY_STAFFID_SQL);
            preparedStatement.setInt(1, staffId);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            if (res.getRow() <= 0 ) return null;
            return processStaffRow(res);
        } catch (SQLException sqlE){
            LOGGER.error("searchForStaffsByDOB: {}", sqlE);
            throw new SQLFaultException("Enable to connect to database when searching for staff by DOB.");
        }
    }

    private List<Staff> processResultStaffProvider(ResultSet res) throws SQLException, ValidationException {
        List<Staff> staffsResult = new ArrayList<>();
        while(res.next()){
            processRow(staffsResult, res);
        }
        return staffsResult;
    }

    private void processRow(List<Staff> staffs, ResultSet res) throws SQLException, ValidationException {
        staffs.add(processStaffRow(res));
    }

    private Staff processStaffRow(ResultSet res) throws SQLException, ValidationException {
        int staffId = res.getInt("id");
        int depId = res.getInt("dep_id");
        String name = res.getString("name");
        Date dob;
        Date startDate;
        Date lastDate;
        String position = res.getString("position");
        String email = res.getString("email");
        String comment = res.getString("comments");
        try {
            String date = res.getString("dob");
            dob = commonConv.stringIsNullOrEmpty(date) ? null : commonConv.getDateFromString(date);
            date = res.getString("start_day");
            startDate = commonConv.stringIsNullOrEmpty(date) ? null :  commonConv.getDateFromString(date);
            date = res.getString("last_day");
            lastDate = commonConv.stringIsNullOrEmpty(date) ? null :   commonConv.getDateFromString(date);
        } catch (ParseException e) {
            throw new ValidationException("Error occuer while converting string to date");
        }
        return  new Staff(staffId, depId, name, dob, startDate, lastDate, position, email, comment);
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
