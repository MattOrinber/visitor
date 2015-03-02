
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>首页图片管理</title>
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
                <label for="search_container_productkey">位置名称：</label> 
                <input type="text" id="search_container_containerName" name="search_container_containerName" class="form-control" style="width: 150px;" value="${param.search_container_containerName}" placeholder="位置名称" />
            </div>
            <button type="submit" class="btn btn-default pull-right">查询</button>
        </form>
    </div>
    <div class="row">
        <table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
            <thead>
                <tr>
	                <th>位置名称</th>
	                <th>位置说明</th>
	                <th>图片路径</th>
	                <th>位置对应城市</th>
	                <th>管理</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${containers.content}" var="container">
                <tr>
                    <tr>
                    <td><a href="${ctx}/container/update/${container.containerId}">${container.containerName}</a></td>
                    <td>${container.containerMoto}</td>
                    <td>${container.containerPicpaths}</td>
                    <td>${container.containerProductkey}</td>
                    <td><a href="${ctx}/container/delete/${container.containerId}">删除</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <tags:pagination page="${containers}" />
    <div class="row"><a class="btn btn-default" href="${ctx}/container/create">创建</a></div>
</body>
</html>
