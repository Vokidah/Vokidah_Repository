package org.hadikov.projectname.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.*;
import org.hadikov.projectname.dto.User;

public class ListenServlet extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();

    public void init(){

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        boolean check=false;

        if(req.getParameter("id")==null) {
            out.print("<h1>");
            for(User user : users) {
                out.print(user.getName()+" "+user.getEmail()+"  ");
            }
            out.print("</h1>");
        }
        else{
            out.print("<h1>");
            for(User user:users){
                if(user.getId()==Integer.parseInt(req.getParameter("id"))){
                    out.print(user.getName()+" "+user.getEmail()+"  ");
                    check=true;
                }
            }
            out.print("</h1>");
            if(!check){
                resp.sendError(404, "Not found!!!" );
            }
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            User user = mapper.readValue(req.getParameter("user"), User.class);

        }catch (IOException e) {

            e.printStackTrace();

        }
    }
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        boolean check=false;
        if(req.getParameter("id")==null) {
            out.print("<h1>Error , can`t find id </h1>");
        }
        else{
            for (Iterator<User> iter = users.listIterator(); iter.hasNext(); ) {
                User a = iter.next();
                if(a.getId()==Integer.parseInt(req.getParameter("id"))) {
                    iter.remove();
                    check=true;
                }
            }
            if(check){out.print("<h1>org.hadikov.projectname.dto.User has been successfully removed</h1>");}
            else{out.print("<h1>Error, element with that id doesn`t exist</h1>");}
        }
    }
}
