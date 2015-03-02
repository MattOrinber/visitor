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
    <form:form id="inputForm" modelAttribute="container" action="${ctx}/container/${action}" method="post" class="form-horizontal">
        <input type="hidden" name="containerId" value="${container.containerId}"/>
        <input type="hidden" name="containerName" value="${container.containerName}"/>
        <fieldset>
            <legend><small>管理</small></legend>
            <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">位置名称:</label>
                <div class="col-sm-10">
                	<label class="form-control">${container.containerName}</label> 
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">位置说明:</label>
                <div class="col-sm-10">
                    <input type="text" id="container_moto" name="containerMoto"  value="${container.containerMoto}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">位置图片地址:</label>
                <div class="col-sm-10">
                    <input type="text" id="container_picpaths" name="containerPicpaths"  value="${container.containerPicpaths}" class="form-control "/>
                </div>
            </div>
            <jsp:include page="../common/city.jsp" flush="true" />
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;    
                    <input id="cancel_btn" class="btn btn-default" type="button" value="返回" onclick="history.back()"/>
                </div>
            </div>
        </fieldset>
    </form:form>
<script type="text/javascript" src="${ctx}/static/common/js/Spider.js"></script>
<script type="text/javascript" src="${ctx}/static/common/js/onload.js"></script>  
</body>
</html>

