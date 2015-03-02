<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>文档管理</title>
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
<%--         <form class="form-inline" action="" id="form">
            <div class="form-group">
                <label for="search_article_desc">：</label> 
                <input type="text" id="search_article_desc" name="search_article_desc" class="form-control" style="width: 150px;" value="${param.search_article_desc}" placeholder="" />
            </div>
            <div class="form-group">
                <label for="search_article_content">：</label> 
                <input type="text" id="search_article_content" name="search_article_content" class="form-control" style="width: 150px;" value="${param.search_article_content}" placeholder="" />
            </div>
            <button type="submit" class="btn btn-default pull-right">查询</button>
        </form> --%>
    </div>
    <div class="row">
        <table id="contentTable" class="table table-striped table-bordered table-condensed table-hover">
            <thead>
                <tr>
                <th>名称</th>
                <th>备注</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${articles.content}" var="article">
                <tr>
                    <tr>
                    <td><a href="${ctx}/article/update/${article.articleId}">${article.articleDesc}</a></td>
                    <td>${article.articleDesc}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <tags:pagination page="${articles}" />
</body>
</html>
