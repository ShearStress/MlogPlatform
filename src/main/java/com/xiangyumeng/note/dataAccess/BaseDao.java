package com.xiangyumeng.note.dataAccess;

import com.xiangyumeng.note.utility.DBUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
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


    /**
     * search for a set
     * 1. get database connection
     * 2. define sql sentence
     * 3. prepare statement
     * 4. if parameters, set them, index from 1
     * 5. execute finding, return a result set
     * 6. get the raw data from result set, (# of records, what are them)
     * 7. analyze result set
     * 8. instantiate an object
     * 9. iterate thru the result set, get every column in the data base
     * 10. using reflection, via column name, get the field object
     * 11. concatinate set, get a string
     * 12. make string a corresponeded set
     * 13. via invoke, call set methods
     * 14. set Javabean into set
     * 15 close resources
     *
     * @param sql sql query
     * @param params collection of parameters
     * @param cls class name
     * @return a list of object
     */
    public static List queryRows(String sql, List<Object> params, Class cls){
        List list = new ArrayList();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // data base connection
            connection = DBUtil.getConnection();
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

            // get meta data
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            // get record number
            int fieldNum = resultSetMetaData.getColumnCount();

            // analyze result set
            while(resultSet.next()){
                //instantiate object
                Object obejct = cls.newInstance();
                obejct.getClass();
                // get all column names
                for (int i = 1; i <= fieldNum; i++){
                    // get column name, something like userID
                    String columnName = resultSetMetaData.getColumnLabel(i);

                    // use column name, get corresponded field
                    Field field = cls.getDeclaredField(columnName);

                    // reflection，use column name and "set" to make a method name, then use the class name to call the function
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);

                    Method method = cls.getDeclaredMethod(setMethod, field.getType());

                    // get the value of that record
                    Object value = resultSet.getObject(columnName);

                    // via invoke, use this set method
                    method.invoke(obejct, value);
                }

                // set Javabean into set
                list.add(obejct);
            }

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            DBUtil.close(resultSet, preparedStatement, connection);
        }
        return list;
    }


    /**
     * search for an object
     * @param sql sql query
     * @param params collection of parameters
     * @param cls class name
     * @return an object
     */
    public static Object queryRow(String sql, List<Object> params, Class cls){
        List list = queryRows(sql, params, cls);
        Object object = null;

        if (list != null && list.size() > 0){
            object = list.get(0);
        }

        return object;
    }
}
