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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import model.LeaveRequest;
import model.User;

@WebServlet("/request/create")
public class CreateRequestServlet extends HttpServlet {

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
        
        request.getRequestDispatcher("createrequest.jsp").forward(request, response);
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
        
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");
        String reason = request.getParameter("reason");
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = new Date(sdf.parse(fromDateStr).getTime());
            Date toDate = new Date(sdf.parse(toDateStr).getTime());
            
            // Validate dates
            if (fromDate.after(toDate)) {
                request.setAttribute("error", "Ngày bắt đầu không thể sau ngày kết thúc");
                request.getRequestDispatcher("createrequest.jsp").forward(request, response);
                return;
            }
            
            if (fromDate.before(new Date(System.currentTimeMillis()))) {
                request.setAttribute("error", "Ngày bắt đầu không thể là ngày trong quá khứ");
                request.getRequestDispatcher("createrequest.jsp").forward(request, response);
                return;
            }
            
            LeaveRequest leaveRequest = new LeaveRequest(user.getUserID(), fromDate, toDate, reason);
            
            if (requestDAO.createRequest(leaveRequest)) {
                request.setAttribute("success", "Tạo đơn xin nghỉ phép thành công!");
                response.sendRedirect("myrequests");
            } else {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo đơn xin nghỉ phép");
                request.getRequestDispatcher("createrequest.jsp").forward(request, response);
            }
            
        } catch (ParseException e) {
            request.setAttribute("error", "Định dạng ngày không hợp lệ");
            request.getRequestDispatcher("createrequest.jsp").forward(request, response);
        }
    }
}