<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="form-group">
    <label for="start_time">开始时间：&nbsp;</label>
    <input id="start_time" name="templateVo.start_time" type="text" readonly="readonly" maxlength="20" class="form-control input Wdate" style="width: 200px; cursor: auto;" value="${start_time}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" placeholder="开始时间"/>
</div>
<div class="form-group">
    <label for="end_time">结束时间：&nbsp;</label>
    <input id="end_time" name="templateVo.end_time" type="text" readonly="readonly" maxlength="20" class="form-control input Wdate" style="width: 200px; cursor: auto;" value="${end_time}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" placeholder="结束时间"/>
</div>
<hr/>

<div class="form-group" id="app_id_div">
    <label for="app_id">平&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;台：</label> 
    <select id="app_id" name="templateVo.app_id" class="form-control" style="width: 120px;" onchange="loadAppidVersion(this.value);">
        <option value="-1">请选择</option>
        <c:if test="${!empty osList}">
            <c:forEach items="${osList}" var="entity">
                <option value="${entity.app_id}">${entity.os_name}</option>
            </c:forEach>
        </c:if>
    </select>   
</div>
<div class="form-group" id="client_version_div">
    <label for="client_version">版&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本：</label> 
    <select id="client_version" name="templateVo.version" class="form-control" style="width: 120px;" >
    </select> 
</div>