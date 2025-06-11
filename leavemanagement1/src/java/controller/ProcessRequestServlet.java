/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.LeaveRequestDAO;
import dal.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import model.LeaveRequest;
import model.User;

@WebServlet("/request/process")
public class ProcessRequestServlet extends HttpServlet {

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
        
        String requestIDStr = request.getParameter("id");
        if (requestIDStr == null) {
            response.sendRedirect("teamrequests");
            return;
        }
        
        try {
            int requestID = Integer.parseInt(requestIDStr);
            LeaveRequest leaveRequest = requestDAO.getRequestByID(requestID);
            
            if (leaveRequest == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đơn xin nghỉ phép");
                return;
            }
            
            // Check if the request belongs to user's subordinate
            User requestUser = userDAO.getUserByID(leaveRequest.getUserID());
            if (requestUser.getManagerID() == null || !requestUser.getManagerID().equals(user.getUserID())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xử lý đơn này");
                return;
            }
            
            request.setAttribute("leaveRequest", leaveRequest);
            request.getRequestDispatcher("processrequest.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect("teamrequests");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("../login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        request.setCharacterEncoding("UTF-8");
        
        String requestIDStr = request.getParameter("requestId");
        String action = request.getParameter("action");
        String note = request.getParameter("note");
        
        try {
            int requestID = Integer.parseInt(requestIDStr);
            String status = "Approved".equals(action) ? "Approved" : "Rejected";
            
            if (requestDAO.processRequest(requestID, status, user.getUserID(), note)) {
                request.setAttribute("success", "Xử lý đơn xin nghỉ phép thành công!");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi xử lý đơn");
            }
            
            response.sendRedirect("teamrequests");
            
        } catch (NumberFormatException e) {
            response.sendRedirect("teamrequests");
        }
    }
}