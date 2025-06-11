<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Tạo Đơn Xin Nghỉ Phép</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }
        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 400px;
        }
        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
        }
        input[type="date"],
        textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box; /* Ensures padding doesn't affect overall width */
        }
        textarea {
            resize: vertical; /* Allow vertical resizing */
            min-height: 80px;
        }
        .error-message {
            color: red;
            margin-bottom: 10px;
            text-align: center;
        }
        .success-message {
            color: green;
            margin-bottom: 10px;
            text-align: center;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 16px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Tạo Đơn Xin Nghỉ Phép</h1>

        <%-- Display error message if present --%>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error-message">${request.getAttribute("error")}</p>
        <% } %>

        <%-- Display success message if present --%>
        <% if (request.getAttribute("success") != null) { %>
            <p class="success-message">${request.getAttribute("success")}</p>
        <% } %>

        <form action="${pageContext.request.contextPath}/request/create" method="POST">
            <div class="form-group">
                <label for="fromDate">Ngày bắt đầu:</label>
                <input type="date" id="fromDate" name="fromDate" 
                       value="${param.fromDate != null ? param.fromDate : ''}" 
                       required>
            </div>

            <div class="form-group">
                <label for="toDate">Ngày kết thúc:</label>
                <input type="date" id="toDate" name="toDate" 
                       value="${param.toDate != null ? param.toDate : ''}" 
                       required>
            </div>

            <div class="form-group">
                <label for="reason">Lý do:</label>
                <textarea id="reason" name="reason" rows="5" required>${param.reason != null ? param.reason : ''}</textarea>
            </div>

            <button type="submit">Gửi Yêu Cầu</button>
        </form>
        
        <a href="${pageContext.request.contextPath}/request/myrequests" class="back-link">Xem Đơn Của Tôi</a>
    </div>
</body>
</html>