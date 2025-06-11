/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.LeaveRequestDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.LeaveRequest;
import model.User;

@WebServlet("/request/myrequests")
public class MyRequestsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LeaveRequestDAO requestDAO = new LeaveRequestDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        List<LeaveRequest> requests = requestDAO.getRequestsByUserID(user.getUserID());
        
        request.setAttribute("requests", requests);
        request.getRequestDispatcher("myrequests.jsp").forward(request, response);
    }
}

