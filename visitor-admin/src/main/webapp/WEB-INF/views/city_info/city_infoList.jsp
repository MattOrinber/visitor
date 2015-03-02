
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
                <label for="search_city_status">名称：</label> 
                <input type="text" id="search_city_name" name="search_city_name" class="form-control" style="width: 150px;" value="${param.search_city_name}" placeholder="" />
            </div>
            <button type="submit" class="btn btn-default pull-right">查询</button>
        </form>
    </div>
    <div class="row">
        <table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
            <thead>
                <tr>
                <th>城市名称</th>
                <th>状态</th>
                <th>管理</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${city_infos.content}" var="city_info">
                <tr>
                	<td width="70%"><a href="${ctx}/city/update/${city_info.cityId}">${city_info.cityName}</a></td>
                    <td width="15%"><c:if test="${city_info.cityStatus == '0'}" >有效</c:if><c:if test="${city_info.cityStatus != '0'}" >无效</c:if></td>
                    <td width="15%"><a href="${ctx}/city/delete/${city_info.cityId}">删除</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <tags:pagination page="${city_infos}" />
    <div class="row"><a class="btn btn-default" href="${ctx}/city/create">创建</a></div>
</body>
</html>
