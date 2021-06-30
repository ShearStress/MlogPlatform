package com.xiangyumeng.note.web;

import com.xiangyumeng.note.persistantObject.Note;
import com.xiangyumeng.note.persistantObject.NoteType;
import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.service.NoteService;
import com.xiangyumeng.note.service.NoteTypeService;
import com.xiangyumeng.note.valueObject.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/note")
public class NoteServlet extends HttpServlet {

    private NoteService noteService = new NoteService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //highlight
        request.setAttribute("menu_page", "note");

        // get user behavior
        String actionName = request.getParameter("actionName");

        //
        if ("view".equals(actionName)){
            noteView(request, response);
        } else if ("addOrUpdate".equals(actionName)){
            // add or update
            addOrUpdate(request, response);
        }
    }


    /**
     * add or update blog
     *             1. receive parameters
     *             2. call method in service layer, return resultInfo
     *             3. check result
     * @param request req
     * @param response resp
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //1. receive parameters
        String typeId = request.getParameter("typeId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        //2. call method in service layer, return resultInfo
        ResultInfo<Note> resultInfo = noteService.addOrUpdate(typeId, title, content);

        //3. check result
        if (resultInfo.getCode() == 1){
            response.sendRedirect("index");
        } else{
            request.setAttribute("resultInfo", resultInfo);
            request.getRequestDispatcher("note?actionName=view").forward(request, response);
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
