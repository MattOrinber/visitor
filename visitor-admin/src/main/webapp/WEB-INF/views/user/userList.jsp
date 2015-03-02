<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>管理</title>
    <script type="text/javascript">
    $(document).ready(function(){
    
    });
    </script>
</head>

<body>
    <c:if test="${not empty message}">
        <div id="message" class="row alert alert-success alert-dismissable"><button data-dismiss="alert" class="close">×</button>${message}</div>
    </c:if>
    <div class="row">
        <form class="form-inline" action="" id="form">
            <div class="form-group">
                <label for="search_user_email">Email：</label> 
                <input type="text" id="search_user_email" name="search_userEmail" class="form-control" style="width: 150px;" value="${param.search_user_email}" placeholder="" />
            </div>
            <div class="form-group">
                <label for="search_user_status">Status：</label> 
               	<select id="search_user_status" name="search_userStatus" class="form-control" style="width: 120px;">
			        <option value="-1">请选择</option>
			        <c:if test="${!empty allStatus}">
	               		<c:forEach items="${allStatus}" var="status">
	               			<option value="${status.key}">${status.value}</option>
						</c:forEach>
			        </c:if>
			    </select>  
            </div>
            <button type="submit" class="btn btn-default pull-right">查询</button>
        </form>
    </div>
    <div class="row">
        <table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
            <thead>
                <tr>
                <th>Email</th>
                <th>Type</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Gender</th>
                <th>Birthday</th>
                <th>Phone Number</th>
                <th>Register Date</th>
                <th>Last Login Time</th>
                <th>Status</th>
                <th>管理</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${users.content}" var="user">
                <tr>
                    <tr>
                    <td><a href="${ctx}/customer/update/${user.userId}">${user.userEmail}</a></td>
                    <td>${user.userType}</td>
                    <td>${user.userFirstName}</td>
                    <td>${user.userLastName}</td>
                    <td>${user.userGender}</td>
                    <td><fmt:formatDate value="${user.userBirthdate}" pattern="yyyy-MM-dd" /></td>
                    <td>${user.userPhonenum}</td>
                    <td><fmt:formatDate value="${user.userRegisterDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    <td><fmt:formatDate value="${user.userLastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    <td>${user.userStatus}</td>
                    <td>
                    	<c:if test="${user.userStatus == '0' }"><a href="${ctx}/user/enable/${user.userId}">启用</a></c:if>
                    	<c:if test="${user.userStatus != '0' }"><a href="${ctx}/user/disable/${user.userId}">禁用</a></c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <tags:pagination page="${users}" />
</body>
</html>
