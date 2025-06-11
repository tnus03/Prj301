/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.LeaveRequestDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.LeaveRequest;
import model.User;

@WebServlet("/request/teamrequests")
public class TeamRequestsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LeaveRequestDAO requestDAO = new LeaveRequestDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check if user has manager role
        if (!userDAO.hasRole(user.getUserID(), "Manager") && 
            !userDAO.hasRole(user.getUserID(), "DivisionLeader")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này");
            return;
        }
        
        List<User> subordinates = userDAO.getAllSubordinates(user.getUserID());
        List<Integer> subordinateIDs = new ArrayList<>();
        for (User subordinate : subordinates) {
            subordinateIDs.add(subordinate.getUserID());
        }
        
        List<LeaveRequest> requests = requestDAO.getRequestsByUserIDs(subordinateIDs);
        
        request.setAttribute("requests", requests);
        request.setAttribute("subordinates", subordinates);
        request.getRequestDispatcher("teamrequests.jsp").forward(request, response);
    }
}
