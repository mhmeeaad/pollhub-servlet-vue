package controllers;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import model.User;
import com.google.gson.Gson;
/**
 *
 * @author y
 */
@WebServlet(urlPatterns = {"/User"})
public class UserController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
         
            String op ;
            op = request.getParameter("op");
            switch (op) {
                case "login":
                    {
                        String username=request.getParameter("username");
                        System.out.println("heeeeeeeeeeeeee");
                        String pass=request.getParameter("password");
                        crud.UserCrud user =new crud.UserCrud();
                        ArrayList<User> users ;
                        users=user.selectById(username);
                        System.out.println("controllers.UserController.processRequest()   "+ users.isEmpty());
                        if (users.isEmpty()==true){
                            
                            response.sendRedirect("user-login.jsp");
                        }
                        else  if (users.get(0).isSuspended == true ){
                            response.sendRedirect("suspended.jsp");
                        }
                        else  if (users.get(0).password.equals(pass)){
                            System.out.println(users.get(0).userId);
                            HttpSession session = request.getSession(true);
                            session.setAttribute("session_username", username);
                            session.setAttribute("session_userid",users.get(0).userId);
                            session.setAttribute("session_IsAdmin",users.get(0).isAdmin );
                            session.setAttribute("session_valid", true);
                            
                            response.sendRedirect("PollController?op=getAllForProfile");
                            
                        }
                        
                        else {
                            request.setAttribute("error", " the user not exist");
                            response.sendRedirect("user-login.jsp");
                        }      break;
                    }
                case "signup":
                    {
                        String email=request.getParameter("email");
                        String username=request.getParameter("username");
                        String pass=request.getParameter("password");
                        crud.UserCrud user =new crud.UserCrud();
                        user.add(username, pass, email, false, true);
                        List<User> users = new ArrayList<>();
                        users=user.selectById(username);
                        HttpSession session = request.getSession(true);
                        session.setAttribute("session_username", username);
                        session.setAttribute("session_userid",users.get(0).userId);
                        session.setAttribute("session_IsAdmin",users.get(0).isAdmin );
                        session.setAttribute("session_valid", "true");
                        response.sendRedirect("PollController?op=getAllForSystem");
                        break;
                    }
                case "logout":
                    {
                        HttpSession session = request.getSession(true);
                        session.invalidate();
                        response.sendRedirect("index.jsp"); // don't forget to create home ya nasser 
                        break;
                    }
                    case "unique":
                    {
                        String username=request.getParameter("username");
                        
                        String pass=request.getParameter("password");
                        crud.UserCrud user =new crud.UserCrud();
                        ArrayList<User> users ;
                        users=user.selectById(username);
                        if (users.isEmpty()){
                             out.println("true");
                        }
                        else {
                        out.println("false");
                        }
                       
                         
                        break;
                    }
                    case "selectAll": {
                    crud.UserCrud user = new crud.UserCrud();
                    List<User> users = user.selectall();
                    Gson gson = new Gson();
                    String output = gson.toJson(users);
                    out.println(output);
                    }
                default:
                    break;
            }
           
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
