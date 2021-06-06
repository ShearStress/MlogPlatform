package com.xiangyumeng.note.dataAccess;

import com.xiangyumeng.note.utility.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * encapsulate basic JDBC operations
 *
 *
 * a basic JDBC operation
 * includes update ( add, remove, modify)
 * and look up( 1. one , only return one thing, normally used to return the total count)
 *              2. search for a set
 *              3. search certain object
 */
public class BaseDao {


    /**
     * update operation, add, remove, modify
     * 1. get database connection
     * 2. define sql sentence
     * 3. prepare statement
     * 4. if parameters, set them, index from 1
     * 5. execute update, return rows that are affects
     * 6. close resources
     *
     * @param sql sql query
     * @param params collection of parameters
     * @return rows that are affected
     */
    public static int executeUpdate(String sql, List<Object> params){
        //number of rows that are affected
        int row = 0;

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DBUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // if there are parameters, then set them
            if (params != null && params.size() > 0){
                // set parameter type to be object, be generic here
                for (int i = 0; i < params.size(); i++){
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }

            row = preparedStatement.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        } finally{
            DBUtil.close(null, preparedStatement, connection);
        }

        return row;
    }


    /**
     *look up( 1. one , only return one thing, normally used to return the total count)
     *              2. search for a set
     *              3. search certain object
     *
     * 1. get database connection
     * 2. define sql sentence
     * 3. prepare statement
     * 4. if parameters, set them, index from 1
     * 5. execute finding, return a result set
     * 6. analyze result set
     *
     * @param sql sql query
     * @param params collection of parameters
     * @return result
     */
    public static Object findSingleValue(String sql, List<Object> params){
        Object object = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // get data base connection
            connection = DBUtil.getConnection();

            // prepare statement
            preparedStatement = connection.prepareStatement(sql);

            // if there are parameters, then set them
            if (params != null && params.size() > 0){
                // set parameter type to be object, be generic here
                for (int i = 0; i < params.size(); i++){
                    preparedStatement.setObject(i+1, params.get(i));
                }
            }

            // execute look up and return result set
            resultSet = preparedStatement.executeQuery();

            // analyze result set
            if (resultSet.next()){
                object = resultSet.getObject(1);
            }


        } catch(Exception e){
            e.printStackTrace();
        } finally {
            // close resources
            DBUtil.close(resultSet, preparedStatement, connection);
        }
        return object;
    }
}
