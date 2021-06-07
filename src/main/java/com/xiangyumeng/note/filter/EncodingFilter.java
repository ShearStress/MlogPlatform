package com.xiangyumeng.note.filter;

import cn.hutool.core.util.StrUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.LogRecord;


/**
 * request for solving messy codes
 * reason:
 *      sever default decoding is ISO-8859-1, this does not support chinese
 *      if you type chinese, there will be messy codes
 *
 * conditions:
 *      POST request
 *          Tomcat 7 and below yes
 *          Tomcat 8 and above yes
 *
 *      GET request
 *          Tomcat 7 and below yes
 *          Tomcat 8 and above no
 *
 * Solution:
 *      POST:
 *          there will be messy codes for whatever version of server,
 *          need to use request.setCharacterEncoding("UTF-8") to set encoding format
 *
 *      GET:
 *          Tomcat 8 and above, there is no need to process
 *          Tomcat 7 and below, use new String(request.getParameter("xxx").getBytes("ISO-8859-1"), "UTF-8");
 */

@WebFilter("/*") // filter all resources
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // based on http
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // process POST request
        request.setCharacterEncoding("UTF-8");

        // request type
        String method = request.getMethod();
        // if is GET
        if ("GET".equalsIgnoreCase(method)){  // ignore case
            // version of server
            String serverInfo = request.getServletContext().getServerInfo();
            // slicing string get version NO
            String version = serverInfo.substring(serverInfo.lastIndexOf("/") + 1, serverInfo.indexOf("."));

            if (version != null && Integer.parseInt(version) < 8){
                // Tomcat 7 and below
                // get request
                MyWrapper myRequest = new MyWrapper(request);

                filterChain.doFilter(myRequest, response);

                return;

            }
        }


        filterChain.doFilter(request, response);
    }


    /**
     * 1. define a class
     * 2. inherit HttpServletRequestWrapper
     * 3. override get parameter
     */
    class MyWrapper extends HttpServletRequestWrapper{


        // fields
        private HttpServletRequest request;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request the {@link HttpServletRequest} to be wrapped.
         * @throws IllegalArgumentException if the request is null
         */
        public MyWrapper(HttpServletRequest request) {
            super(request);
            this.request = request;
        }


        /**
         * override, deal with messy codes
         * @param name parameter name
         * @return a modified
         */
        @Override
        public String getParameter(String name) {

            // get parameter(messy)
            String value = request.getParameter(name);

            if (StrUtil.isBlank(value)){
                return value;
            }

            // not blank, use new String()
            try{
                value = new String(value.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } catch(Exception e){
                e.printStackTrace();
            }

            return value;
        }
    }

}
