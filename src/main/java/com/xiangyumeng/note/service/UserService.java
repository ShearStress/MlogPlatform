package com.xiangyumeng.note.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xiangyumeng.note.dataAccess.UserDao;
import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.valueObject.ResultInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

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
        }

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


    /**
     * check uniquness
     *             1. if empty?
     *             2. empty, return 0
     *             3. call function in Dao, use userID and nickName, get use object
     *             4. if user object exist, return 0
     *             4. else return 1.
     * @param nick nick
     * @param userId user id
     * @return right 1, not 0
     */
    public Integer checkNick(String nick, Integer userId) {
        //1. if empty?
        if (StrUtil.isBlank(nick)){
            return 0;
        }
        //2. empty, return 0

        //3. call function in Dao, use userID and nickName, get use object
        User user = userDao.queryUserByNickAndId(nick, userId);

        //4. if user object exist, return 0
        if (user != null){
            return 0;
        }

        //4. else return 1.
        return 1;
    }

    /**
     * modify user info
     *             1. get parameters (nickname, mood)
     *             2. non-empty check
     *                 1） nick name is empty. send error and state code to resultInfo and return
     *
     *             3. get user object from session field (get user default icon)
     *             4. uploading file
     *                 1) get part object, request.getPart("name");
     *                 2) via part object, upload file name
     *                 3) check if file name empty
     *                 4) if not, get path of file
     *             5. update user icon
     *             6. user dao layer function, rerturn rows that are affected
     *             7. check rows value
     *             8. return
     * @param request
     * @return
     */
    public ResultInfo<User> updateUser(HttpServletRequest request) {
        ResultInfo<User> resultInfo = new ResultInfo<>();
        //1. get parameters (nickname, mood)
        String nick = request.getParameter("nick");
        String mood = request.getParameter("mood");

        //2. non-empty check
        if (StrUtil.isBlank(nick)){
            resultInfo.setCode(0);
            resultInfo.setMsg("user nick name can not be empty");
            return resultInfo;
        }

        // 3. get user object from session field (get user default icon)
        User user = (User) request.getSession().getAttribute("user");
        user.setNick(nick);
        user.setMood(mood);

        //4. uploading file
        try{
            //1) get part object, request.getPart("name");
            Part part = request.getPart("img");

            //2) via part object, upload file name
            String header = part.getHeader("Content-Disposition");
            String str = header.substring(header.lastIndexOf("=") + 2);

            //3) check if file name empty
            String fileName = str.substring(0, str.length()-1);

            if (!StrUtil.isBlank(fileName)){
                //4) if not, get path of file
                //5. update user icon
                String filePath = request.getServletContext().getRealPath("/WEB-INF/upload/");
                part.write(filePath + "/" + fileName);
            }


        } catch(Exception e){
            e.printStackTrace();
        }


        //6. user dao layer function, return rows that are affected
        int row = userDao.updateUser(user);

        //7. check rows value
        if (row>0){
            resultInfo.setCode(1);
            request.getSession().setAttribute("user", user);
        } else{
            resultInfo.setCode(0);
            resultInfo.setMsg("update fail!");
        }
        //8. return
        return resultInfo;
    }
}
