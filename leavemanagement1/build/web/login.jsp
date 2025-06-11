<%-- 
    Document   : Login
    Created on : Jun 11, 2025, 9:01:58 AM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - Hệ thống quản lý nghỉ phép</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <div class="card shadow mt-5">
                    <div class="card-header bg-primary text-white text-center">
                        <h4>Đăng nhập hệ thống</h4>
                    </div>
                    <div class="card-body">
                        <% if (request.getAttribute("error") != null) { %>
                            <div class="alert alert-danger">
                                <%= request.getAttribute("error") %>
                            </div>
                        <% } %>
                        
                        <form method="post" action="login">
                            <div class="mb-3">
                                <label for="username" class="form-label">Tên đăng nhập:</label>
                                <input type="text" class="form-control" id="username" name="username" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Mật khẩu:</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">Đăng nhập</button>
                            </div>
                        </form>
                        
                        <div class="mt-3">
                            <small class="text-muted">
                                <strong>Tài khoản demo:</strong><br>
                                Admin: admin/123456<br>
                                Trưởng phòng: mrA/123456<br>
                                Quản lý: mrB/123456<br>
                                Nhân viên: mrTeo/123456
                            </small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>

