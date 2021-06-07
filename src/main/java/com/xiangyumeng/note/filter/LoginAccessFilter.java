package com.xiangyumeng.note.filter;


import com.xiangyumeng.note.persistantObject.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

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
 */
@WebFilter("/*")
public class LoginAccessFilter implements Filter {
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


        // none of the conditions meet, block the request, redirect to login page
        response.sendRedirect("login.jsp");

    }

    @Override
    public void destroy() {

    }
}
