package com.xiangyumeng.note.web;


import cn.hutool.core.io.FileUtil;
import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.service.UserService;
import com.xiangyumeng.note.valueObject.ResultInfo;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;

@WebServlet("/user")
@MultipartConfig
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
        // set index navigation high light
        req.setAttribute("menu_page", "user");

        // receive user behavior
        String actionName = req.getParameter("actionName");

        // tell user behavior, call corresponded methods
        if ("login".equals(actionName)){
            //user login
            userLogin(req,resp);
        }

        else if ("logout".equals(actionName)){
            //user logout
            userLogOut(req, resp);
        }

        else if ("userCenter".equals(actionName)){
            // user center page
            userCenter(req, resp);
        }
        
        else if ("userHead".equals(actionName)){
            //loading user icon
            userHead(req, resp);
        }

        else if ("checkNick".equals(actionName)){
            //uniqueness
            checkNick(req, resp);
        }

        else if ("updateUser".equals(actionName)){
            // update user info
            updateUser(req, resp);
        }


    }


    /**
     * update user info
     * @param request req
     * @param response resp
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo<User> resultInfo = userService.updateUser(request);

        request.setAttribute("resultInfo", resultInfo);

        request.getRequestDispatcher("user?actionName=userCenter").forward(request, response);


    }


    /**
     * check uniqueness
     *             1. get parameter
     *             2. get user info from session
     *             3. use method in service layer, get result
     *             4. output stream, respond to ajax method
     *             5. close resource
     * @param request request
     * @param response response
     */
    private void checkNick(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. get parameter
        String nick = request.getParameter("nick");

        //2. get user info from session
        User user = (User) request.getSession().getAttribute("user");

        //3. use method in service layer, get result
        Integer code = userService.checkNick(nick, user.getUserId());

        //4. output stream, respond to ajax method
        response.getWriter().write(code+"");

        //5. close resource
        response.getWriter().close();
    }


    /**
     * loading icons
     *     1. get parameters (image name)
     *     2. get path for the image (request.getServletContext().getRealPath("/");
     *     3. via image path, get file object
     *     4. get postfix of the image
     *     5. via different postfix, set different types of response
     *     6. FileUtils.copy(), copy the image to browser
     * @param request request
     * @param response response
     */
    private void userHead(HttpServletRequest request, HttpServletResponse response) throws IOException {
         //1. get parameters (image name)
        String head = request.getParameter("imageName");

         //2. get path for the image (request.getServletContext().getRealPath("/");
        String realPath = request.getServletContext().getRealPath("/WEB-INF/upload/");

         //3. via image path, get file object
        File file = new File(realPath + "/" + head);

         //4. get postfix of the image
        String pic = head.substring(head.lastIndexOf(".") + 1);

         //5. via different postfix, set different types of response
        if ("PNG".equalsIgnoreCase(pic)){
            response.setContentType("image/png");
        } else if("JPG".equalsIgnoreCase(pic) || "JPEG".equalsIgnoreCase(pic)){
            response.setContentType("image/jpeg");
        } else if ("GIF".equalsIgnoreCase(pic)){
            response.setContentType("image/gif");
        }

         //6. FileUtils.copy(), copy the image to browser
        FileUtils.copyFile(file, response.getOutputStream());
    }


    /**
     * get into user center
     *      1. set index dynamically include page value
     *      2??? request to redirect to index
     * @param request request
     * @param response response
     */
    private void userCenter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. set index dynamically include page value
        request.setAttribute("changePage", "user/info.jsp");

        //2.  request to redirect to index
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }


    /**
     * user logout
     *    1. destroy session object
     *    2. delete cookie object
     *    3. redirect to log in page
     * @param request request
     * @param response respond
     */
    private void userLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1. destroy session object
        request.getSession().invalidate();

        //2. delete cookie object
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        //3. redirect to log in page
        response.sendRedirect("login.jsp");
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
     * @param request a servlet request
     * @param response a servlet response
     */


    private void userLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. ???????????? ?????????????????????
        String userName = request.getParameter("userName");
        String userPwd = request.getParameter("userPwd");

        // 2. ??????Service?????????????????????ResultInfo??????
        ResultInfo<User> resultInfo = userService.userLogin(userName, userPwd);

        // 3. ????????????????????????
        if (resultInfo.getCode() == 1) { // ????????????
            //  ????????????????????????session????????????
            request.getSession().setAttribute("user", resultInfo.getResult());
            //  ???????????????????????????????????????rem?????????1???
            String rem = request.getParameter("rem");
            // ??????????????????????????????????????????cookie????????????????????????????????????????????????
            if ("1".equals(rem)) {
                // ??????Cookie??????
                Cookie cookie = new Cookie("user",userName +"-"+userPwd);
                // ??????????????????
                cookie.setMaxAge(3*24*60*60);
                // ??????????????????
                response.addCookie(cookie);
            } else {
                // ???????????????????????????cookie??????
                Cookie cookie = new Cookie("user", null);
                // ??????cookie?????????max age???0
                cookie.setMaxAge(0);
                // ??????????????????
                response.addCookie(cookie);
            }
            // ??????????????????index??????
            response.sendRedirect("index");

        } else { // ??????
            // ???resultInfo???????????????request????????????
            request.setAttribute("resultInfo", resultInfo);
            // ?????????????????????????????????
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }


}
