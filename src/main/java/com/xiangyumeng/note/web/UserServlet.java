package com.xiangyumeng.note.web;


import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.service.UserService;
import com.xiangyumeng.note.valueObject.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();

    /**
     * Base on use behavior, call corresponded methods
     * @param req a servlet request
     * @param resp a servlet response
     * @throws ServletException exception1
     * @throws IOException exception2
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // receive user behavior
        String actionName = req.getParameter("actionName");

        // tell user behavior, call corresponded methods
        if ("login".equals(actionName)){
            //user login
            userLogin(req,resp);
        }
    }

    /**
     * USER LOGIN
     *        1. get parameter, name, password
     *         2. use method in service layer, return a ResultInfo object
     *         3. tell if log in successfully
     *             fail:
     *                 put ResultInfo to request
     *
     *                 redirect to login page
     *             success:
     *                 store user information to session
     *                 if user choose to remember me, (rem=1), store user information(name, pwd) to cookie and set invalid time
     *                 and respond to client ; if not, clean the original cookie information.
     *
     *                 redirect to index page.
     * @param req a servlet request
     * @param resp a servlet response
     */
    private void userLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. get parameters
        String userName = req.getParameter("userName");
        String userPwd = req.getParameter("userPwd");

        // 2. use method in service layer, return a ResultInfo object
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        // 3. tell if log in successfully
        if (resultInfo.getCode() == 1){ // success
            //store user information to session
            req.setAttribute("user", resultInfo.getResult());

            //if user choose to remember me, (rem=1), store user information(name, pwd) to cookie and set invalid time
            String rem = req.getParameter("rem");
            if ("1".equals(rem)){
                // get cookie object
                Cookie cookie = new Cookie("user", userName + "-" + userPwd);

                //set valid period
                cookie.setMaxAge(3*24*60*60);

                // respond to client
                resp.addCookie(cookie);
            } else{  // don't remember pwd
                // and respond to client ; if not, clean the original cookie information.
                Cookie cookie = new Cookie("user", null);
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }

            // redirect to index page
            resp.sendRedirect("index.jsp");

        } else{       // fail
            //put ResultInfo to request
            //redirect to login page
            req.setAttribute("resultInfo", resultInfo);
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
