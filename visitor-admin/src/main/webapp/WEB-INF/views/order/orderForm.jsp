<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.visitor.appportal.web.utils.OrderInfo" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>订单明细</title>
	<script>
		$(document).ready(function() {
			//聚焦第一个输入框
			$("#test_msg").focus();
			//为inputForm注册validate函数
			$("#inputForm").validate();

			/*$("#fulfill").click(function(event) {
]				$.ajax({
					url: '${ctx}/order/fulfill',
					type: 'POST',
					dataType: 'json',
					data: {orderId: 'orderId'},
				})
				.done(function() {
					console.log("success");
				})
				.fail(function() {
					console.log("error");
				})
				.always(function() {
					console.log("complete");
				});
				
			});*/
		});
	</script>
</head>

<body>
	<div class="row">
		<div class="col-md-12">
			<form action="${ctx}/order/fulfill" method="post">
				<input name="orderId" type="hidden" value="${detail.order.orderId}" />
				<c:choose>
					<c:when test="${detail.orderStatus == 6 }">
						<a href="javascript:;" class="btn default" id="gritter-regular">REFUNDED</a>
					</c:when>
					<c:otherwise>
						<button id="fulfill" onclick="" class="btn blue" type="submit">Fulfill</button>
					</c:otherwise>
				</c:choose>
			</form>
		</div>
	</div>
	<br/>
	<div class="row">
		<div class="col-md-12">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption"><i class="fa fa-reorder"></i>订单明细</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
						<thead><tr>
						<th>Product Name</th>
						<th>Start Date</th>
						<th>End Date</th>
						<th>Price</th>
						<th>Amount</th></tr>
						</thead>
						<tbody>
						<c:if test="${empty detail.product}">
							<tr>
								<td colspan="6">暂无数据</td>
							</tr>
						</c:if>
						<tr>
							<td width="30%"><a href="${ctx}/product/update/${detail.product.productId}">${detail.product.productOverviewtitle}</a></td>
							<td width="15%"><fmt:formatDate value="${detail.order.orderStartDate}" pattern="yyyy-MM-dd" /></td>
							<td width="15%"><fmt:formatDate value="${detail.order.orderEndDate}" pattern="yyyy-MM-dd" /></td>
							<td width="20%"><fmt:formatNumber value="${detail.product.productBaseprice}" pattern="#,###.00#" /></td>
							<td width="10%">${detail.order.orderTotalCount}</td>
						</tr>
						<tr>
							<td colspan="6" class="text-right">Total Cost:<fmt:formatNumber value="${detail.order.orderTotalAmount}" pattern="#,###.00#"/> ${detail.order.orderCurrency}</td>
						</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption"><i class="fa fa-reorder"></i>Customer Info</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal" role="form">
						<div class="form-body">
							<div class="form-group">
								<label class="col-md-3 control-label">First Name</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="First Name" value="${detail.customer.userFirstName}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Last Name</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Last Name" value="${detail.customer.userLastName}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Email</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Email" value="${detail.customer.userEmail}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Phonenum</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Phonenum" value="${detail.customer.userPhonenum}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Address</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Address" value="${detail.customer.userAddress}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">PayPal</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="PayPal Number" value="${detail.customer.userPaypalnum}">
								</div>
							</div>
						</div>
						<div class="form-actions fluid">
							<div class="col-md-offset-3 col-md-9">
								<button type="button" class="btn default" onclick="window.location.href='${ctx}/customer/update/${detail.customer.userId}'">Detail</button>                              
							</div>
						</div>
					</form>
				</div>	
			</div>
		</div>
		<div class="col-md-6">
			<div class="portlet box green">
				<div class="portlet-title">
					<div class="caption"><i class="fa fa-reorder"></i>Seller Info</div>
					<div class="tools">
						<a href="javascript:;" class="collapse"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal" role="form">
						<div class="form-body">
							<div class="form-group">
								<label class="col-md-3 control-label">First Name</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="First Name" value="${detail.seller.userFirstName}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Last Name</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Last Name" value="${detail.seller.userLastName}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Email</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Email" value="${detail.seller.userEmail}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Phonenum</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Phonenum" value="${detail.seller.userPhonenum}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">Address</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="Address" value="${detail.seller.userAddress}">
								</div>
							</div>
							<div class="form-group">
								<label class="col-md-3 control-label">PayPal</label>
								<div class="col-md-9">
									<input type="text" class="form-control" placeholder="PayPal Number" value="${detail.seller.userPaypalnum}">
								</div>
							</div>
						</div>
						<div class="form-actions fluid">
							<div class="col-md-offset-3 col-md-9">
								<button type="button" class="btn default" onclick="window.location.href='${ctx}/customer/update/${detail.customer.userId}'">Detail</button>                              
							</div>
						</div>
					</form>
				</div>		
			</div>	
		</div>
	</div>
</body>
</html>
