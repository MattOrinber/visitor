<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>订单管理</title>
	<script type="text/javascript">
	<script>
	$(document).ready(function() {
		$("#account-tab").addClass("active");
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
				<label for="search_sellerName">Seller Name：</label> 
				<input type="text" id="search_sellerName" name="search_sellerName" class="form-control" style="width: 150px;" value="${param.search_sellerName}" placeholder="Seller Name" />
			</div>
			<div class="form-group">
				<label for="search_customerName">Customer Name：</label> 
				<input type="text" id="search_customerName" name="search_customerName" class="form-control" style="width: 150px;" value="${param.search_customerName}" placeholder="Customer Name" />
			</div>
			<div class="form-group">
				<label for="beginDate">日期范围：&nbsp;</label>
				<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="form-control input Wdate" style="width: 200px; cursor: auto;" value="${beginDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" placeholder="日期"/>
			</div>
			<button type="button" class="btn btn-default pull-right" onclick="submitSearch();">查询</button>
		</form>
	</div>
    <p/>
	<div id="progress" class="row progress" style="display: none;">
        <div class="progress-bar progress-bar-success"></div>
    </div>
	<div class="row">
		<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
			<thead><tr>
			<th>Order</th>
			<th>Date</th>
			<th>Seller Name</th>
			<th>Customer Name</th>
			<th>Status</th>
			<th>Total Cost</th></tr>
			</thead>
			<tbody>
			<c:if test="${empty orders.content}">
				<tr>
					<td colspan="2">暂无数据</td>
				</tr>
			</c:if>
			<c:forEach items="${orders.content}" var="order">
				<tr>
					<td width="10%"><a href="${ctx}/order/update/${order.orderId}">${order.orderId}</a></td>
					<td width="20%">${order.orderStartDate}<fmt:formatDate value="${order.orderCreateDate}" pattern="yyyy-MM-dd hh:mm:ss" /></td>
					<td width="20%"><a href="${ctx}/user/${order.sellerId}">${order.sellerName}</a></td>
					<td width="20%"><a href="${ctx}/user/${order.customerId}">${order.customerName}</a></td>
					<td width="10%"><a href="${ctx}/order/update/${order.status}">${order.status}</a></td>
					<td width="20%"><fmt:formatNumber value="${order.orderTotalAmount}" pattern="#,###.00#"/> ${order.orderCurrency}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<tags:pagination page="${orders}" />
</body>
</html>
