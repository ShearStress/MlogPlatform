package com.xiangyumeng.note.filter;


import com.xiangyumeng.note.persistantObject.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * blocking illegal access
 *  resources to block:
 *      all /*
 *
 *  recourses that can pass:
 *      1. certain pages - pages that user can access when not logged in, like login.jsp, register.jsp..
 *      2. static resources - staticResources, like js, css, images...
 *      3. let pass certain behaviors - operations use can do without logging in, like actionName = logIn...
 *      4. when user has logged in - check session, see whether there is user info, if not, redirect to login.jsp
 *
 *  automatic login, if user previously choose to remember him
 *      via cookie,
 *      when user is in log-out state and requesting pages that need log-in state, then request
 *      for automatic log-in
 *
 *      purpose:
 *          let user maintain a log-in state
 *
 *      realization:
 *          from cookie object, get user name and password, automatically log in
 *          1. get cookie array
 *          2. see if its empty
 *          3. if not, get cookie whose name is user
 *          4. get the value - name and password, userName-userPwd
 *          5. separate to be name and password - split("-")
 *          6. request to log in  (actionName = "login" )
 *          7. return
 *
 */
@WebFilter("/*")
public class LoginAccessFilter implements Filter {
    // have to override functions to make WebFilter work :(
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // base on HTTP
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // get path
        String path = request.getRequestURI(); // projectPath/resources path

        //1. certain pages - pages that user can access when not logged in, like login.jsp, register.jsp..
        if (path.contains("/login.jsp")){
            filterChain.doFilter(request, response);
            return;
        }

        //2. static resources - staticResources, like js, css, images...
        if (path.contains("/staticResources")){
            filterChain.doFilter(request, response);
            return;
        }

        //3. let pass certain behaviors - operations use can do without logging in, like actionName = logIn...
        if (path.contains("/user")){
            // get user behavior
            String actionName = request.getParameter("actionName");
            if ("login".equals(actionName)){
                filterChain.doFilter(request, response);
                return;
            }
        }

        //4. when user has logged in - check session, see whether there is user info, if not, redirect to login.jsp
        // get user object in session filed
        User user = (User) request.getSession().getAttribute("user");

        // if not empty
        if (user != null){
            filterChain.doFilter(request, response);
            return;
        }


        // automatically log in, if there is a cookie
        //from cookie object, get user name and password, automatically log in
        //1. get cookie array
        Cookie[] cookies = request.getCookies();

        //2. see if its empty
        if (cookies != null && cookies.length > 0){
            //3. if not, get cookie whose name is user
            for (Cookie cookie:cookies){
                //4. get the value - name and password, userName-userPwd
                if ("user".equals(cookie.getName())){
                    String value = cookie.getValue();

                    //5. separate to be name and password - split("-")
                    String[] val = value.split("-");

                    //6. request to log in  (actionName = "login" )
                    String userName = val[0];
                    String userPwd = val[1];

                    String url = "user?actionName=login&rem=1&userName=" + userName + "&userPwd=" + userPwd;
                    request.getRequestDispatcher(url).forward(request,response);
                    return;
                }


            }
        }



        // none of the conditions meet, block the request, redirect to login page
        response.sendRedirect("login.jsp");

    }

    @Override
    public void destroy() {

    }
}
