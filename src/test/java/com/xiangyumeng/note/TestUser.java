package com.xiangyumeng.note;

import com.xiangyumeng.note.dataAccess.UserDao;
import com.xiangyumeng.note.persistantObject.User;
import org.junit.Test;

public class TestUser {

    @Test
    public void testQueryUserByName(){
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }
}
