package com.xiangyumeng.note.utility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Author: Xiangyu meng
 *
 * define methods to connect to database, close database...
 * a class of datavase utilities
 */
public class DBUtil {

    private static Properties properties = new Properties();

    // static block, this block will be executed when the class is loaded
    static{
        // load configuration, using an input stream
        InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");

        //load configurations to properties object
        try {
            properties.load(in);

            //using properties object, to get name
            Class.forName(properties.getProperty("jdbcName"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * a static method, convenient to call from other classes
     * get data base connection
     * @return a connection to the database
     */
    public static Connection getConnection(){
        Connection connection = null;

        //get information of database from properties object
        String dbUrl = properties.getProperty("dbUrl");
        String dbName = properties.getProperty("dbName");
        String dbPwd = properties.getProperty("dbPwd");

        //acquire database connection
        try {
            connection = DriverManager.getConnection(dbUrl, dbName, dbPwd);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }


    /**
     * close all the resources
     * @param resultSet, a collection of results
     * @param preparedStatement
     * @param connection, the conenction to database
     */
    public static void close(ResultSet resultSet,
                             PreparedStatement preparedStatement,
                             Connection connection){

        // close all the resources, if they exist.
        try {
            if (resultSet != null) {resultSet.close();}
            if (preparedStatement != null) {preparedStatement.close();}
            if (connection != null) {connection.close();}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
