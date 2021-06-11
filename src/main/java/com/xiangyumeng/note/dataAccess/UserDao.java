package com.xiangyumeng.note.dataAccess;

import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.utility.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    /**
     * data access layer
     */

    /**
     * find object based on user name
     * 1. sql sentence
     * 2. set parameters
     * 3, use methods in baseDao
     * @param userName user name
     * @return result
     */
    public User queryUserByName(String userName){
        //1. define sql
        String sql = "select * from tb_user where uname = ?";

        //2. set parameters
        List<Object> params = new ArrayList<>();
        params.add(userName);

        //3. userDao methods
        User user = (User)BaseDao.queryRow(sql, params, User.class);

        return user;
    }




    /**
     *         using user name, return user object
     *         1. get data base connection
     *         2. define sql sentences
     *         3. pre compile - prepare statement
     *         4. set parameters
     *         5. execute look up, return result
     *         6. analysis result set
     *         7. close resources
     * @param userName user name
     * @return a User object
     */
    public User queryUserByName02(String userName){
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            // 1. get data base connection
            connection = DBUtil.getConnection();

            // 2. define sql sentence
            String sql = "select * from tb_user where uname = ?";

            // 3. prepare statement
            preparedStatement = connection.prepareStatement(sql);

            // 4. set parameters
            preparedStatement.setString(1, userName);

            // 5. do the look up, return a resultSet
            resultSet = preparedStatement.executeQuery();

            // 6. analyze result

            // encapsulate all data into a user object
            if (resultSet.next()){
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUname(userName);
                user.setHead(resultSet.getString("head"));
                user.setMood(resultSet.getString("mood"));
                user.setNick(resultSet.getString("nick"));
                user.setUpwd((resultSet.getString("upwd")));
            }

        } catch(Exception e){
            e.printStackTrace();
        } finally {
            // 7. close resources
            DBUtil.close(resultSet, preparedStatement, connection);
        }



        return user;
    }
}
