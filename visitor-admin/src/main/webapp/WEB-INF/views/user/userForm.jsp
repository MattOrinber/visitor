<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springside.org.cn/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>User管理</title>
    <script>
        $(document).ready(function() {
        });
    </script>
</head>

<body>
    <form:form id="inputForm" modelAttribute="user" action="${ctx}/customer/${action}" method="post" class="form-horizontal">
        <input type="hidden" name="userId" value="${user.userId}"/>
        <fieldset>
            <legend><small>管理</small></legend>
            <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Email:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_email" name="userEmail"  value="${user.userEmail}" class="form-control required"/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Facebook ID:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_facebookid" name="userFacebookId"  value="${user.userFacebookId}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Type:</label>
                <div class="col-sm-10">
                	<c:forEach items="${allUserTypes}" var="type">
						<label for="type${type.key}" class="radio">
							<input id="type${type.key}" name="userType" type="radio" value="${type.key}" <c:if test="${user.userType==type.key}">checked="true"</c:if>  class="required" /> ${type.value}&nbsp;
						</label>
					</c:forEach>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">First Name:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_firstname" name="userFirstName"  value="${user.userFirstName}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Last Name:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_lastname" name="userLastName"  value="${user.userLastName}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Gender:</label>
                <div class="col-sm-10">
                	<c:forEach items="${allGenders}" var="gender">
						<label for="gender${gender.key}" class="radio">
							<input id="gender${gender.key}" name="userGender" type="radio" value="${gender.key}" <c:if test="${user.userGender==gender.key}">checked="true"</c:if>  class="required" /> ${gender.value}&nbsp;
						</label>
					</c:forEach>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Birthday:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_birthdate" name="userBirthdate"  value='<fmt:formatDate value="${user.userBirthdate}" pattern="yyyy-MM-dd" />' class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Phone Number:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_phonenum" name="userPhonenum"  value="${user.userPhonenum}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Address:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_address" name="userAddress"  value="${user.userAddress}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">School:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_school" name="userSchool"  value="${user.userSchool}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Work:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_work" name="userWork"  value="${user.userWork}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Timezone:</label>
                <div class="col-sm-10">
                	<select id="user_timezone" name="userTimeZone" class="form-control">
				        <option value="-1">请选择</option>
				        <c:if test="${not empty allTimeZones}">
				            <c:forEach items="${allTimeZones}" var="entity">
				                <option value="${entity.timeZoneId}" <c:if test="${entity.timeZoneId==user.userTimeZone}">selected='selected'</c:if> >${entity.timeZoneName}</option>
				            </c:forEach>
				        </c:if>
				    </select>   
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Language:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_language" name="userLanguage"  value="${user.userLanguage}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Emergency:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_emergency" name="userEmergency"  value="${user.userEmergency}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Photourl:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_photourl" name="userPhotourl"  value="${user.userPhotourl}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Status:</label>
                <div class="col-sm-10">
                	<c:forEach items="${allStatus}" var="status">
						<label for="status${status.key}" class="radio">
							<input id="status${status.key}" name="userStatus" type="radio" value="${status.key}" <c:if test="${user.userStatus==status.key}">checked="true"</c:if>  class="required" /> ${status.value}&nbsp;
						</label>
					</c:forEach>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Paypal num:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_paypalnum" name="userPaypalnum"  value="${user.userPaypalnum}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">Detail:</label>
                <div class="col-sm-10">
                    <input type="text" id="user_detail" name="userDetail"  value="${user.userDetail}" class="form-control "/>
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