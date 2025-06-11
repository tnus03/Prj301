<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đơn xin nghỉ phép của tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .status-badge {
            font-size: 0.85em;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="../dashboard">
                <i class="fas fa-calendar-alt me-2"></i>
                Hệ thống nghỉ phép
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">
                    Xin chào, ${sessionScope.user.fullName}
                </span>
                <a class="nav-link" href="../logout">
                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2>
                        <i class="fas fa-file-alt"></i>
                        Đơn xin nghỉ phép của tôi
                    </h2>
                    <a href="create" class="btn btn-primary">
                        <i class="fas fa-plus"></i>
                        Tạo đơn mới
                    </a>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty requests}">
                                <div class="text-center py-5">
                                    <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                    <h5 class="text-muted">Chưa có đơn xin nghỉ phép nào</h5>
                                    <p class="text-muted">Hãy tạo đơn xin nghỉ phép đầu tiên của bạn</p>
                                    <a href="create" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> Tạo đơn mới
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-dark">
                                            <tr>
                                                <th>STT</th>
                                                <th>Ngày bắt đầu</th>
                                                <th>Ngày kết thúc</th>
                                                <th>Số ngày</th>
                                                <th>Lý do</th>
                                                <th>Trạng thái</th>
                                                <th>Ngày tạo</th>
                                                <th>Ghi chú</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach items="${requests}" var="request" varStatus="status">
                                                <tr>
                                                    <td>${status.index + 1}</td>
                                                    <td>
                                                        <fmt:formatDate value="${request.fromDate}" pattern="dd/MM/yyyy"/>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${request.toDate}" pattern="dd/MM/yyyy"/>
                                                    </td>
                                                    <td>
                                                        <c:set var="days" value="${(request.toDate.time - request.fromDate.time) / (1000 * 60 * 60 * 24) + 1}"/>
                                                        <fmt:formatNumber value="${days}" pattern="#"/>
                                                    </td>
                                                    <td>
                                                        <div class="text-truncate" style="max-width: 200px;" 
                                                             title="${request.reason}">
                                                            ${request.reason}
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${request.status == 'Pending'}">
                                                                <span class="badge bg-warning status-badge">
                                                                    <i class="fas fa-clock"></i> Chờ duyệt
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${request.status == 'Approved'}">
                                                                <span class="badge bg-success status-badge">
                                                                    <i class="fas fa-check"></i> Đã duyệt
                                                                </span>
                                                            </c:when>
                                                            <c:when test="${request.status == 'Rejected'}">
                                                                <span class="badge bg-danger status-badge">
                                                                    <i class="fas fa-times"></i> Từ chối
                                                                </span>
                                                            </c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${request.createdDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty request.managerNote}">
                                                                <span class="text-truncate d-inline-block" 
                                                                      style="max-width: 150px;" 
                                                                      title="${request.managerNote}">
                                                                    ${request.managerNote}
                                                                </span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted">--</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-3">
            <div class="col-12">
                <a href="../dashboard" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i>
                    Quay lại Dashboard
                </a>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>