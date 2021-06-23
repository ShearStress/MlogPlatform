package com.xiangyumeng.note.web;

import com.xiangyumeng.note.persistantObject.NoteType;
import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.service.NoteTypeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/note")
public class NoteServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //highlight
        request.setAttribute("menu_page", "note");

        // get user behavior
        String actionName = request.getParameter("actionName");

        //
        if ("view".equals(actionName)){
            noteView(request, response);
        }
    }


    /**
     * get into blog publish page
     * @param request req
     * @param response resp
     */
    private void noteView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get user object
        User user = (User) request.getSession().getAttribute("user");

        // get type list
        List<NoteType> typeList = new NoteTypeService().findTypeList(user.getUserId());

        // set into request
        request.setAttribute("typeList", typeList);

        // dynamically include
        request.setAttribute("changePage", "note/view.jsp");

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
