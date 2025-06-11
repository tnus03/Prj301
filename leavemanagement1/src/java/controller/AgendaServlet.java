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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import model.LeaveRequest;
import model.User;

@WebServlet("/agenda")
public class AgendaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LeaveRequestDAO requestDAO = new LeaveRequestDAO();
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check if user has division leader role
        if (!userDAO.hasRole(user.getUserID(), "DivisionLeader")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập trang này");
            return;
        }
        
        // Get date range from parameters or use default (current month)
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");
        
        Calendar cal = Calendar.getInstance();
        Date fromDate, toDate;
        
        if (fromDateStr != null && toDateStr != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                fromDate = new Date(sdf.parse(fromDateStr).getTime());
                toDate = new Date(sdf.parse(toDateStr).getTime());
            } catch (ParseException e) {
                // Use default dates if parsing fails
                cal.set(Calendar.DAY_OF_MONTH, 1);
                fromDate = new Date(cal.getTimeInMillis());
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                toDate = new Date(cal.getTimeInMillis());
            }
        } else {
            // Default to current month
            cal.set(Calendar.DAY_OF_MONTH, 1);
            fromDate = new Date(cal.getTimeInMillis());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            toDate = new Date(cal.getTimeInMillis());
        }
        
        // Get all users in the department
        List<User> departmentUsers = userDAO.getUsersByDepartment(user.getDepartmentID());
        List<Integer> userIDs = new ArrayList<>();
        for (User deptUser : departmentUsers) {
            userIDs.add(deptUser.getUserID());
        }
        
        // Get approved leave requests in the date range
        List<LeaveRequest> leaveRequests = requestDAO.getApprovedRequestsByDateRange(userIDs);
        
        // Create agenda data structure
        Map<Integer, Map<String, String>> agendaData = createAgendaData(departmentUsers, leaveRequests, fromDate, toDate);
        List<String> dateHeaders = createDateHeaders(fromDate, toDate);
        
        request.setAttribute("users", departmentUsers);
        request.setAttribute("agendaData", agendaData);
        request.setAttribute("dateHeaders", dateHeaders);
        request.setAttribute("fromDate", fromDate);
        request.setAttribute("toDate", toDate);
        
        request.getRequestDispatcher("agenda.jsp").forward(request, response);
    }
    
    private Map<Integer, Map<String, String>> createAgendaData(List<User> users, List<LeaveRequest> leaveRequests, Date fromDate, Date toDate) {
        Map<Integer, Map<String, String>> agendaData = new HashMap<>();
        
        // Initialize all users with "present" status for all dates
        for (User user : users) {
            Map<String, String> userDates = new HashMap<>();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);
            
            while (!cal.getTime().after(toDate)) {
                String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                userDates.put(dateStr, "present"); // Green - present
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            
            agendaData.put(user.getUserID(), userDates);
        }
        
        // Update with leave requests
        for (LeaveRequest request : leaveRequests) {
            if ("Approved".equals(request.getStatus())) {
                Map<String, String> userDates = agendaData.get(request.getUserID());
                if (userDates != null) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(request.getFromDate());
                    
                    while (!cal.getTime().after(request.getToDate())) {
                        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
                        if (userDates.containsKey(dateStr)) {
                            userDates.put(dateStr, "leave"); // Red - on leave
                        }
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
            }
        }
        
        return agendaData;
    }
    
    private List<String> createDateHeaders(Date fromDate, Date toDate) {
        List<String> headers = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        
        while (!cal.getTime().after(toDate)) {
            String dateStr = new SimpleDateFormat("d/M").format(cal.getTime());
            headers.add(dateStr);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return headers;
    }
}
