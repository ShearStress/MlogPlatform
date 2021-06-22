package com.xiangyumeng.note.web;

import com.alibaba.fastjson.JSON;
import com.xiangyumeng.note.persistantObject.NoteType;
import com.xiangyumeng.note.persistantObject.User;
import com.xiangyumeng.note.service.NoteTypeService;
import com.xiangyumeng.note.utility.JsonUtil;
import com.xiangyumeng.note.valueObject.ResultInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/type")
public class NoteTypeServlet extends HttpServlet {

    private NoteTypeService typeService = new NoteTypeService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // high light value
        request.setAttribute("menu_page", "type");

        //get user behavior
        String actionName = request.getParameter("actionName");

        // detect user behavior
        if ("list".equals(actionName)){
            // search for type list
            typeList(request, response);

        }

        else if ("delete".equals(actionName)){
            deleteType(request, response);
        }

        else if ("addOrUpdate".equals(actionName)){
            addOrUpdate(request, response);
        }
    }

    /**
     *            1. receive parameters
     *             2. get user object from session, get userID
     *             3. use update method in service layer, return an resultInfo object
     *             4. respond to ajax
     * @param request req
     * @param response resp
     */
    private void addOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        //1. receive parameters
        String typeName = request.getParameter("typeName");
        String typeId = request.getParameter("typeId");

        //2. get user object from session, get userID
        User user = (User) request.getSession().getAttribute("user");

        //3. use update method in service layer, return an resultInfo object
        ResultInfo<Integer> resultInfo = typeService.addOrUpdate(typeName, user.getUserId(), typeId);

        //4. respond to ajax
        JsonUtil.toJson(response, resultInfo);
    }


    /**
     * delete type
     * @param request req
     * @param response resp
     */
    private void deleteType(HttpServletRequest request, HttpServletResponse response) {
        //1. receive params
        String typeId = request.getParameter("typeId");

        //2. update operation in service layer, return resultInfo
        ResultInfo<NoteType> resultInfo = typeService.deleteType(typeId);

        //3. convert resultInfo object to json format String, respond to ajax method
        // respond type
        JsonUtil.toJson(response, resultInfo);
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
