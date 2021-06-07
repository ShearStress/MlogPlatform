package com.xiangyumeng.note.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/index")
public class IndexServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse respond) throws ServletException, IOException {
        // set index, dynamically included
        request.setAttribute("changePage", "note/list.jsp");

        request.getRequestDispatcher("index.jsp").forward(request,respond);
    }
}
