/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

/**
 *
 * @author ACER
 */
import model.Department;
import java.sql.*;
import java.util.*;

public class DepartmentDAO {
    
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM Department ORDER BY DepartmentName";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Department dept = new Department();
                dept.setDepartmentID(rs.getInt("DepartmentID"));
                dept.setDepartmentName(rs.getString("DepartmentName"));
                dept.setDescription(rs.getString("Description"));
                departments.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }
    
    public Department getDepartmentByID(int departmentID) {
        String sql = "SELECT * FROM Department WHERE DepartmentID = ?";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, departmentID);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Department dept = new Department();
                dept.setDepartmentID(rs.getInt("DepartmentID"));
                dept.setDepartmentName(rs.getString("DepartmentName"));
                dept.setDescription(rs.getString("Description"));
                return dept;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
