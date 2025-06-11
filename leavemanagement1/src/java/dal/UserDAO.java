/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author ACER
 */
import java.sql.*;
import java.util.*;
import model.*;

public class UserDAO {
    
    public User authenticate(String username, String password) {
        String sql = "SELECT u.*, d.DepartmentName, m.FullName as ManagerName " +
                    "FROM [User] u " +
                    "LEFT JOIN Department d ON u.DepartmentID = d.DepartmentID " +
                    "LEFT JOIN [User] m ON u.ManagerID = m.UserID " +
                    "WHERE u.Username = ? AND u.Password = ? AND u.IsActive = 1";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = mapUser(rs);
                user.setRoles(getRolesByUserID(user.getUserID()));
                user.setUsername(rs.getString("Username"));
                user.setFullName(rs.getString("FullName"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Role> getRolesByUserID(int userID) {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT r.* FROM Role r " +
                    "INNER JOIN UserRole ur ON r.RoleID = ur.RoleID " +
                    "WHERE ur.UserID = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getInt("RoleID"));
                role.setRoleName(rs.getString("RoleName"));
                role.setDescription(rs.getString("Description"));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }
    
    public List<User> getSubordinates(int managerID) {
        List<User> subordinates = new ArrayList<>();
        String sql = "SELECT u.*, d.DepartmentName " +
                    "FROM [User] u " +
                    "LEFT JOIN Department d ON u.DepartmentID = d.DepartmentID " +
                    "WHERE u.ManagerID = ? AND u.IsActive = 1";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, managerID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                subordinates.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subordinates;
    }
    
    public List<User> getAllSubordinates(int managerID) {
        List<User> allSubordinates = new ArrayList<>();
        List<User> directSubordinates = getSubordinates(managerID);
        
        for (User subordinate : directSubordinates) {
            allSubordinates.add(subordinate);
            allSubordinates.addAll(getAllSubordinates(subordinate.getUserID()));
        }
        
        return allSubordinates;
    }
    
    public User getUserByID(int userID) {
        String sql = "SELECT u.*, d.DepartmentName, m.FullName as ManagerName " +
                    "FROM [User] u " +
                    "LEFT JOIN Department d ON u.DepartmentID = d.DepartmentID " +
                    "LEFT JOIN [User] m ON u.ManagerID = m.UserID " +
                    "WHERE u.UserID = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> getUsersByDepartment(int departmentID) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, d.DepartmentName " +
                    "FROM [User] u " +
                    "LEFT JOIN Department d ON u.DepartmentID = d.DepartmentID " +
                    "WHERE u.DepartmentID = ? AND u.IsActive = 1";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, departmentID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public boolean hasRole(int userID, String roleName) {
        String sql = "SELECT COUNT(*) FROM UserRole ur " +
                    "INNER JOIN Role r ON ur.RoleID = r.RoleID " +
                    "WHERE ur.UserID = ? AND r.RoleName = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userID);
            ps.setString(2, roleName);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setFullName(rs.getString("FullName"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setDepartmentID(rs.getInt("DepartmentID"));
        user.setDepartmentName(rs.getString("DepartmentName"));
        
        int managerID = rs.getInt("ManagerID");
        if (!rs.wasNull()) {
            user.setManagerID(managerID);
        }
        
        user.setManagerName(rs.getString("ManagerName"));
        user.setCreatedDate(rs.getDate("CreatedDate"));
        user.setActive(rs.getBoolean("IsActive"));
        
        return user;
    }
}