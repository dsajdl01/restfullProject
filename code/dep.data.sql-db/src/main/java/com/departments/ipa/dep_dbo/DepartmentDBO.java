package com.departments.ipa.dep_dbo;
//        com.departments/ipa/dep_dbo/DepartmentDBO.java

import java.sql.*;
import java.util.Properties;
/**
 * Hello world!
 *
 */
public class DepartmentDBO  {
   private final String dbClassName = "com.mysql.jdbc.Driver";

    private final String CONNECTION = "jdbc:mysql://127.0.0.1/department_db";

    public String getData() throws ClassNotFoundException,SQLException {

        // Class.forName(xxx) loads the jdbc classes and
        // creates a drivermanager class factory
        Class.forName(dbClassName);

        // Properties for user and password.
        Properties p = new Properties();
        p.put("user","root");
        p.put("password","dsajdl0112345");

        // Now to connect
        Connection c = DriverManager.getConnection(CONNECTION,p);
        Statement con = c.createStatement();
        ResultSet resSet = con.executeQuery("SELECT * FROM department");
        String result = "";
        while(resSet.next()){
            result = "id: " + resSet.getString("id") + " / name: " + resSet.getString("name") + ",";
        }
        c.close();
        return  result;
  //      return "djhkgjkghd,";
    }
}