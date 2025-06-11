<%-- 
    Document   : dashboard
    Created on : Jun 11, 2025, 9:03:21 AM
    Author     : ACER
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Hệ thống quản lý nghỉ phép</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .dashboard-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            height: 100%;
        }
        .dashboard-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
        .card-icon {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 24px;
            color: white;
            margin-bottom: 1rem;
        }
        .icon-primary { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
        .icon-success { background: linear-gradient(135deg, #56ab2f 0%, #a8e6cf 100%); }
        .icon-warning { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
        .icon-info { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
        
        .welcome-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
        }
    </style>
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="dashboard">
                <i class="fas fa-calendar-check me-2"></i>
                Hệ thống quản lý nghỉ phép
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">
                    <i class="fas fa-user me-1"></i>
                    Xin chào, ${user.fullName}
                </span>
                <a class="nav-link" href="logout">
                    <i class="fas fa-sign-out-alt me-1"></i>
                    Đăng xuất
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="welcome-section">
            <h2 class="h3 mb-2">Chào mừng trở lại!</h2>
            <p class="mb-0">Quản lý các yêu cầu nghỉ phép của bạn một cách hiệu quả</p>
        </div>

        <div class="row g-4">
            <!-- Create Request Card -->
            <div class="col-md-6 col-lg-3">
                <div class="card dashboard-card">
                    <div class="card-body text-center p-4">
                        <div class="card-icon icon-primary mx-auto">
                            <i class="fas fa-plus"></i>
                        </div>
                        <h5 class="card-title">Tạo đơn nghỉ phép</h5>
                        <p class="card-text text-muted">Tạo yêu cầu nghỉ phép mới</p>
                        <a href="request/create" class="btn btn-primary">
                            <i class="fas fa-plus me-1"></i>
                            Tạo đơn
                        </a>
                    </div>
                </div>
            </div>

            <!-- My Requests Card -->
            <div class="col-md-6 col-lg-3">
                <div class="card dashboard-card">
                    <div class="card-body text-center p-4">
                        <div class="card-icon icon-success mx-auto">
                            <i class="fas fa-list"></i>
                        </div>
                        <h5 class="card-title">Đơn của tôi</h5>
                        <p class="card-text text-muted">Xem tất cả đơn nghỉ phép</p>
                        <a href="request/myrequests" class="btn btn-success">
                            <i class="fas fa-eye me-1"></i>
                            Xem đơn
                        </a>
                    </div>
                </div>
            </div>

            <!-- Team Requests Card (Only for managers) -->
            <c:if test="${userDAO.hasRole(user.userID, 'Manager') || userDAO.hasRole(user.userID, 'DivisionLeader')}">
                <div class="col-md-6 col-lg-3">
                    <div class="card dashboard-card">
                        <div class="card-body text-center p-4">
                            <div class="card-icon icon-warning mx-auto">
                                <i class="fas fa-users"></i>
                            </div>
                            <h5 class="card-title">Đơn của team</h5>
                            <p class="card-text text-muted">Quản lý đơn của nhân viên</p>
                            <a href="request/team-requests" class="btn btn-warning">
                                <i class="fas fa-tasks me-1"></i>
                                Quản lý
                            </a>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Agenda Card (Only for division leaders) -->
            <c:if test="${userDAO.hasRole(user.userID, 'DivisionLeader')}">
                <div class="col-md-6 col-lg-3">
                    <div class="card dashboard-card">
                        <div class="card-body text-center p-4">
                            <div class="card-icon icon-info mx-auto">
                                <i class="fas fa-calendar-alt"></i>
                            </div>
                            <h5 class="card-title">Lịch phòng ban</h5>
                            <p class="card-text text-muted">Xem lịch làm việc phòng ban</p>
                            <a href="agenda" class="btn btn-info">
                                <i class="fas fa-calendar me-1"></i>
                                Xem lịch
                            </a>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>

        <!-- Quick Stats Section -->
        <div class="row mt-5">
            <div class="col-12">
                <div class="card dashboard-card">
                    <div class="card-body">
                        <h5 class="card-title mb-4">
                            <i class="fas fa-chart-bar me-2"></i>
                            Thông tin nhanh
                        </h5>
                        <div class="row text-center">
                            <div class="col-md-4">
                                <div class="p-3">
                                    <h3 class="text-primary mb-1">
                                        <i class="fas fa-clock"></i>
                                    </h3>
                                    <h5 class="mb-0">Đơn chờ duyệt</h5>
                                    <small class="text-muted">Các đơn đang chờ xử lý</small>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="p-3">
                                    <h3 class="text-success mb-1">
                                        <i class="fas fa-check-circle"></i>
                                    </h3>
                                    <h5 class="mb-0">Đơn đã duyệt</h5>
                                    <small class="text-muted">Các đơn đã được chấp thuận</small>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="p-3">
                                    <h3 class="text-danger mb-1">
                                        <i class="fas fa-times-circle"></i>
                                    </h3>
                                    <h5 class="mb-0">Đơn bị từ chối</h5>
                                    <small class="text-muted">Các đơn không được chấp thuận</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
