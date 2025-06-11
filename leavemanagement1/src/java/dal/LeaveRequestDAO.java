/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author ACER
 */
import model.LeaveRequest;
import java.sql.*;
import java.util.*;

public class LeaveRequestDAO {
    
    public boolean createRequest(LeaveRequest request) {
        String sql = "INSERT INTO LeaveRequest (UserID, FromDate, ToDate, Reason, Status, CreatedDate) " +
                    "VALUES (?, ?, ?, ?, ?, GETDATE())";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, request.getUserID());
            ps.setDate(2, request.getFromDate());
            ps.setDate(3, request.getToDate());
            ps.setString(4, request.getReason());
            ps.setString(5, "InProgress");
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<LeaveRequest> getRequestsByUserID(int userID) {
        List<LeaveRequest> requests = new ArrayList<>();
        String sql = "SELECT lr.*, u.Username, u.FullName as UserFullName, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequest lr " +
                    "INNER JOIN [User] u ON lr.UserID = u.UserID " +
                    "LEFT JOIN [User] p ON lr.ProcessedBy = p.UserID " +
                    "WHERE lr.UserID = ? " +
                    "ORDER BY lr.CreatedDate DESC";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                requests.add(mapLeaveRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public List<LeaveRequest> getRequestsByUserIDs(List<Integer> userIDs) {
        if (userIDs.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<LeaveRequest> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lr.*, u.Username, u.FullName as UserFullName, ");
        sql.append("p.FullName as ProcessedByName ");
        sql.append("FROM LeaveRequest lr ");
        sql.append("INNER JOIN [User] u ON lr.UserID = u.UserID ");
        sql.append("LEFT JOIN [User] p ON lr.ProcessedBy = p.UserID ");
        sql.append("WHERE lr.UserID IN (");
        
        for (int i = 0; i < userIDs.size(); i++) {
            sql.append("?");
            if (i < userIDs.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") ORDER BY lr.CreatedDate DESC");
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < userIDs.size(); i++) {
                ps.setInt(i + 1, userIDs.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requests.add(mapLeaveRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public LeaveRequest getRequestByID(int requestID) {
        String sql = "SELECT lr.*, u.Username, u.FullName as UserFullName, " +
                    "p.FullName as ProcessedByName " +
                    "FROM LeaveRequest lr " +
                    "INNER JOIN [User] u ON lr.UserID = u.UserID " +
                    "LEFT JOIN [User] p ON lr.ProcessedBy = p.UserID " +
                    "WHERE lr.RequestID = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, requestID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapLeaveRequest(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean processRequest(int requestID, String status, int processedBy, String note) {
        String sql = "UPDATE LeaveRequest SET Status = ?, ProcessedBy = ?, ProcessedDate = GETDATE(), " +
                    "ProcessingNote = ? WHERE RequestID = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, status);
            ps.setInt(2, processedBy);
            ps.setString(3, note);
            ps.setInt(4, requestID);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<LeaveRequest> getApprovedRequestsByDateRange(List<Integer> userIDs) {
        if (userIDs.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<LeaveRequest> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lr.*, u.Username, u.FullName as UserFullName ");
        sql.append("FROM LeaveRequest lr ");
        sql.append("INNER JOIN [User] u ON lr.UserID = u.UserID ");
        sql.append("WHERE lr.UserID IN (");
        
        for (int i = 0; i < userIDs.size(); i++) {
            sql.append("?");
            if (i < userIDs.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(") AND lr.Status = 'Approved' ");
        sql.append("AND ((lr.FromDate <= ? AND lr.ToDate >= ?) ");
        sql.append("OR (lr.FromDate >= ? AND lr.FromDate <= ?)) ");
        sql.append("ORDER BY lr.FromDate");
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            for (int userID : userIDs) {
                ps.setInt(paramIndex++, userID);
            }
        
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                requests.add(mapLeaveRequest(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    private LeaveRequest mapLeaveRequest(ResultSet rs) throws SQLException {
        LeaveRequest request = new LeaveRequest();
        request.setRequestID(rs.getInt("RequestID"));
        request.setUserID(rs.getInt("UserID"));
        request.setUserName(rs.getString("Username"));
        request.setUserFullName(rs.getString("UserFullName"));
        request.setFromDate(rs.getDate("FromDate"));
        request.setToDate(rs.getDate("ToDate"));
        request.setReason(rs.getString("Reason"));
        request.setStatus(rs.getString("Status"));
        request.setCreatedDate(rs.getTimestamp("CreatedDate"));
        
        int processedBy = rs.getInt("ProcessedBy");
        if (!rs.wasNull()) {
            request.setProcessedBy(processedBy);
        }
        
        request.setProcessedByName(rs.getString("ProcessedByName"));
        request.setProcessedDate(rs.getTimestamp("ProcessedDate"));
        request.setProcessingNote(rs.getString("ProcessingNote"));
        
        return request;
    }
}

