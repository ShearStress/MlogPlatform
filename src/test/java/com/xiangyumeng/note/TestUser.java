package com.xiangyumeng.note;

import com.xiangyumeng.note.dataAccess.BaseDao;
import com.xiangyumeng.note.dataAccess.UserDao;
import com.xiangyumeng.note.persistantObject.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestUser {

    @Test
    public void testQueryUserByName(){
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }

    @Test
    public void testAdd(){
        String sql = "insert into tb_user (uname, upwd, nick, head, mood) values (?, ?, ?, ?, ?)";
        List<Object> params = new ArrayList<>();
        params.add("michael");
        params.add("e10adc3949ba59abbe56e057f20f883e");
        params.add("King");
        params.add("404.jpg");
        params.add("sunny day huh?");
        int row = BaseDao.executeUpdate(sql, params);
        System.out.println(row);

    }
}
