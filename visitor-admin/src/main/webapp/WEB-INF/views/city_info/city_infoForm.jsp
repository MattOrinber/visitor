
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springside.org.cn/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>管理</title>
    <script>
        $(document).ready(function() {
            //聚焦第一个输入框
            $("#loginName").focus();
        });
        
    </script>
</head>

<body>
    <form:form id="inputForm" modelAttribute="city_info" action="${ctx}/city/${action}" method="post" class="form-horizontal">
        <input type="hidden" name="cityId" value="${city_info.cityId}"/>
        <fieldset>
            <legend><small>管理</small></legend>
            <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
            <div class="form-group">
                <label for="cityName" class="col-sm-2 control-label">城市名称:</label>
                <div class="col-sm-10">
                    <input type="text" id="cityName" name="cityName"  value="${city_info.cityName}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="cityStatus" class="col-sm-2 control-label">状态:</label>
                <div class="col-sm-10">
                	<c:forEach items="${allStatus}" var="status">
						<label for="status${status.key}" class="radio">
							<input id="status${status.key}" name="cityStatus" type="radio" value="${status.key}" <c:if test="${city_info.cityStatus==status.key}">checked="true"</c:if>  class="required" /> ${status.value}&nbsp;
						</label>
					</c:forEach>
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

