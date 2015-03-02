<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
                <label for="search_product_hometype">Home Type：</label> 
                <input type="text" id="search_product_hometype" name="search_productHometype" class="form-control" style="width: 150px;" value="${param.search_productHometype}" placeholder="Home Type" />
            </div>
            <div class="form-group">
                <label for="search_product_roomtype">Room Type：</label> 
                <input type="text" id="search_product_roomtype" name="search_productRoomType" class="form-control" style="width: 150px;" value="${param.search_productRoomType}" placeholder="Room Type" />
            </div>
         	<div class="form-group">
                <label for="search_product_accomodates">Title：</label> 
                <input type="text" id="search_productOverviewtitle" name="search_productOverviewtitle" class="form-control" style="width: 150px;" value="${param.search_productOverviewtitle}" placeholder="Title" />
            </div>
            <button type="submit" class="btn btn-default pull-right">查询</button>
        </form>
    </div>
    <div class="row">
        <table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
            <thead>
                <tr>
                <th>Home Type</th>
                <th>Room Type</th>
                <th>Title</th>
                <th>Accomodates</th>
                <th>Base Price</th>
                <th>Currency</th>
                <th>City</th>
                <th>Create Date</th>
                <th>管理</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${products.content}" var="product">
                <tr>
                    <tr>
                    <td width="10%">
                    	<c:if test="${empty product.productHometype}"><a href="${ctx}/product/update/${product.productId}">empty type</a></c:if>
                    	<c:if test="${not empty product.productHometype}"><a href="${ctx}/product/update/${product.productId}">${product.productHometype}</a></c:if>
                    </td>
                    <td width="10%">${product.productRoomType}</td>
                    <td width="15%">
                   		<c:if test='${fn:length(product.productOverviewtitle) < 30 }'>${product.productOverviewtitle}</c:if>
                   		<c:if test='${fn:length(product.productOverviewtitle) >= 30 }'>${fn:substring(product.productOverviewtitle, 0, 30)}...</c:if>
                    </td>
                    <td width="5%">${product.productAccomodates}</td>
                    <td width="5%">${product.productBaseprice}</td>
                    <td width="5%">${product.productCurrency}</td>
                    <td width="5%">${product.productCity}</td>
                    <td width="15%"><fmt:formatDate value="${product.productCreateDate}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                    <td width="5%"><a href="${ctx}/product/delete/${product.productId}">删除</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <tags:pagination page="${products}" />
    <div class="row"><a class="btn btn-default" href="${ctx}/product/create">创建</a></div>
</body>
</html>
