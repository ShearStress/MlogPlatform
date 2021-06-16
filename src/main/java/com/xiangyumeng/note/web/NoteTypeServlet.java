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

@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {

    private NoteTypeService typeService = new NoteTypeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get user behavior
        String actionName = request.getParameter("actionName");

        // detect user behavior
        if ("list".equals(actionName)){
            // search for type list
            typeList(request, response);

        }
    }

    /**
     *  search for user type list
     *              1. get user object in session field
     *             2. user search method in service layer, search for the collection of types of current log-in user, return collection.
     *             3. put type list into request field
     *             4. index dynamically included value
     *             5. request to redirect to index.jsp
     * @param request req
     * @param response resp
     */
    private void typeList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. get user object in session field
        User user = (User) request.getSession().getAttribute("user");

        //2. user search method in service layer, search for the collection of types of current log-in user, return collection.
        List<NoteType> typeList = typeService.findTypeList(user.getUserId());

        //3. put type list into request field
        request.setAttribute("typeList", typeList);

        //4. index dynamically included value
        request.setAttribute("changePage", "type/list.jsp");

        //5. request to redirect to index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
