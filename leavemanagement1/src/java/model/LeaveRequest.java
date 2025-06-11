/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ACER
 */
import java.sql.Date;
import java.sql.Timestamp;

public class LeaveRequest {
    private int requestID;
    private int userID;
    private String userName;
    private String userFullName;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private String status; // InProgress, Approved, Rejected
    private Timestamp createdDate;
    private Integer processedBy;
    private String processedByName;
    private Timestamp processedDate;
    private String processingNote;
    
    // Constructors
    public LeaveRequest() {}
    
    public LeaveRequest(int userID, Date fromDate, Date toDate, String reason) {
        this.userID = userID;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.status = "InProgress";
    }
    
    // Getters and Setters
    public int getRequestID() { return requestID; }
    public void setRequestID(int requestID) { this.requestID = requestID; }
    
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    
    public String getUserFullName() { return userFullName; }
    public void setUserFullName(String userFullName) { this.userFullName = userFullName; }
    
    public Date getFromDate() { return fromDate; }
    public void setFromDate(Date fromDate) { this.fromDate = fromDate; }
    
    public Date getToDate() { return toDate; }
    public void setToDate(Date toDate) { this.toDate = toDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
    
    public Integer getProcessedBy() { return processedBy; }
    public void setProcessedBy(Integer processedBy) { this.processedBy = processedBy; }
    
    public String getProcessedByName() { return processedByName; }
    public void setProcessedByName(String processedByName) { this.processedByName = processedByName; }
    
    public Timestamp getProcessedDate() { return processedDate; }
    public void setProcessedDate(Timestamp processedDate) { this.processedDate = processedDate; }
    
    public String getProcessingNote() { return processingNote; }
    public void setProcessingNote(String processingNote) { this.processingNote = processingNote; }
    
    // Helper method to get duration in days
    public long getDurationDays() {
        if (fromDate != null && toDate != null) {
            long diffInMillies = toDate.getTime() - fromDate.getTime();
            return diffInMillies / (1000 * 60 * 60 * 24) + 1; // +1 to include both start and end date
        }
        return 0;
    }
}
