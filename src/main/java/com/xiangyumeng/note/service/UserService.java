package com.xiangyumeng.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xiangyumeng.note.dataAccess.UserDao;
import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.valueObject.ResultInfo;

public class UserService {

    private UserDao userDao = new UserDao();


    /**
     *         1. tell if empty parameters:
     *             if empty:
     *                 set a state code, and notification
     *                 return ResultInfo
     *
     *         2. if not empty:
     *             look up via user name and password
     *
     *         3. tell if user object is empty:
     *             if empty:
     *                set a state code, and notification
     *                return ResultInfo
     *
     *             if not:
     *                 compare the password given by database with the one given by front end(password should be protected)
     *                 if (password is wrong):
     *                     set a state code, and notification
     *                     put ResultInfo in request
     *                     send to login page
     *                 else:
     *                     set a state code, and notification
     *                     put ResultInfo in request
     *                     send to login page
     * @param userName, user name
     * @param userPwd, typed in password
     * @return a encapsulated result infomation, need to be unpacked
     */
    public ResultInfo<User> userLogin(String userName, String userPwd){
       ResultInfo<User> resultInfo =  new ResultInfo<>();

       // display wrong info when login failed
        User u = new User();
        u.setUname(userName);
        u.setUpwd(userPwd);
        resultInfo.setResult(u);


       // 1. tell if empty parameters
       if (StrUtil.isBlank(userName) || StrUtil.isBlank(userPwd)){
            //set a state code, and notification
            //return ResultInfo
            resultInfo.setCode(0);
            resultInfo.setMsg("User name or password can not be empty!");
            return resultInfo;
       }

        // 2. if not empty, look up via user name and password
        User user = userDao.queryUserByName(userName);

        // 3. tell if user object is empty:
        if (user == null){
            //set a state code, and notification
            //return ResultInfo
            resultInfo.setCode(0);
            resultInfo.setMsg("This user does no exist!");
            return resultInfo;
        } else{
            // not empty, compare password
            // first encrypt the password given by front-end using MD5 algorithm, since the password stored in database is encrypted.
            userPwd = DigestUtil.md5Hex(userPwd);

            // tell whether the encrypted password match the record in database.
            if(!user.getUpwd().equals(userPwd)){
                resultInfo.setCode(0);
                resultInfo.setMsg("Wrong password!");
                return resultInfo;
            }
            resultInfo.setCode(1);
            resultInfo.setResult(user);
            return resultInfo;

        }


    }
}
