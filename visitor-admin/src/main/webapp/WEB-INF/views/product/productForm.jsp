<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springside.org.cn/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Product Info</title>
    <script>
        $(document).ready(function() {
            //聚焦第一个输入框
            $("#loginName").focus();
        });
        
    </script>
</head>

<body>
    <form:form id="inputForm" modelAttribute="product" action="${ctx}/product/${action}" method="post" class="form-horizontal">
        <input type="hidden" name="productId" value="${product.productId}"/>
        <fieldset>
            <legend><small>管理</small></legend>
            <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Home Type:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_hometype" name="productHometype"  value="${product.productHometype}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Room Type:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_roomtype" name="productRoomType"  value="${product.productRoomType}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Accomodates:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_accomodates" name="productAccomodates"  value="${product.productAccomodates}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Available Type:</label>
                <div class="col-sm-10">
                	<c:forEach items="${allAvailableTypes}" var="types">
						<label for="types${types.key}" class="radio">
							<input id="types${types.key}" name="productAvailabletype" type="radio" value="${types.key}" <c:if test="${product.productAvailabletype==types.key}">checked="true"</c:if>  class="required" /> ${types.value}&nbsp;
						</label>
					</c:forEach>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Base Price:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_baseprice" name="productBaseprice"  value="${product.productBaseprice}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Currency:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_currency" name="productCurrency"  value="${product.productCurrency}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Overview Title:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_overview_title" name="productOverviewtitle"  value="${product.productOverviewtitle}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Photo Path:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_photo_paths" name="productPhotopaths"  value="${product.productPhotopaths}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Amenities:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_amenities" name="productAmenities"  value="${product.productAmenities}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Room Number:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_roomnum" name="productRoomnum"  value="${product.productRoomnum}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Bed Number:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_bedsnum" name="productBedsnum"  value="${product.productBedsnum}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Bathroom Number:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_bathroomnum" name="productBathroomnum"  value="${product.productBathroomnum}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Address:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_addressid" name="productAddressid"  value="${product.productAddressid}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Price Per Week:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_priceperweek" name="productPriceperweek"  value="${product.productPriceperweek}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Price Per Month:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_pricepermonth" name="productPricepermonth"  value="${product.productPricepermonth}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Term Min Stay:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_termminstay" name="productTermminstay"  value="${product.productTermminstay}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Term Max Stay:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_termmaxstay" name="productTermmaxstay"  value="${product.productTermmaxstay}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Check In After:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_checkinafter" name="productCheckinafter"  value="${product.productCheckinafter}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Check Out Before:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_checkoutbefore" name="productCheckoutbefore"  value="${product.productCheckoutbefore}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Cancelation Policy:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_cancelationpolicy" name="productCancellationpolicy"  value="${product.productCancellationpolicy}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Avail Key:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_availkey" name="productAvailkey"  value="${product.productAvailkey}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Status:</label>
                <div class="col-sm-10">
                	<c:forEach items="${allStatus}" var="status">
						<label for="status${status.key}" class="radio">
							<input id="status${status.key}" name="productStatus" type="radio" value="${status.key}" <c:if test="${product.productStatus==status.key}">checked="true"</c:if>  class="required" /> ${status.value}&nbsp;
						</label>
					</c:forEach>                	
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">City:</label>
                <div class="col-sm-10">
                    <input type="text" id="product_city" name="productCity"  value="${product.productCity}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Create Date:</label>
                <div class="col-sm-10">
                    <fmt:formatDate value="${product.productCreateDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Update Date:</label>
                <div class="col-sm-10">
                    <fmt:formatDate value="${product.productUpdateDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Publish User:</label>
                <div class="col-sm-10">
                	${product.productPublishUserId}
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Publish User Email:</label>
                <div class="col-sm-10">
                    ${product.productPublishUserEmail}
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;    
                    <input id="cancel_btn" class="btn btn-default" type="button" value="返回" onclick="history.back()"/>
                </div>
            </div>
        </fieldset>
    </form:form>
</body>
</html>

