<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springside.org.cn/tags/form" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>管理</title>
    <script type="text/javascript" src="${ctx}/static/ckedit/ckeditor.js"></script>
    <script>
        $(document).ready(function() {
            //聚焦第一个输入框
            $("#article_content").focus();
            CKEDITOR.replace( 'article_content' );
            //setProductDescEditor();
        });
        
    	// product page editor change to ckEditor
        function setProductDescEditor() {
        	// product
        	var node = $("#article_content");
        	if (node != null) {
        		productDetailStrGlobal = $("#article_content").attr("data-inner");
        		CKEDITOR.replace( 'productOverviewDetailStr', {
        			on: {
        				focus: false,
        				blur: onProductDescAndTitle,
        				instanceReady:setProductDiscValue
        			},
        			coreStyles_bold: { 
        				element: 'b' 
        			},
        			coreStyles_italic: { 
        				element: 'i' 
        			},
        	
        			fontSize_style: {
        				element: 'font',
        				attributes: { 'size': '#(size)' }
        			},
        			width:800,
        			height:250
        		});
        	}
        }
    	
        function onProductDescAndTitle() {
        	if (ifCanUpdate) {
        		var titleStr = $.trim($("#productTitle").val());
        		if (titleStr != "") {
        			var editor = CKEDITOR.instances.productOverviewDetailStr;
        			var editorProductDesc = $.trim(editor.getData());
        			
        			var originDescStr = $("#productDetailDESCInfoStr").attr("data-inner");
        			if (editorProductDesc != "" && editorProductDesc != originDescStr) {
        				var productDetail = {};
        				var productIdStr = $("#productIdPageTemp").html();
        				
        				productDetail.productIdStr = productIdStr;
        				productDetail.productOverviewTitleStr = titleStr;
        				productDetail.productOverviewDetailStr = editorProductDesc;
        			    
        			    var urlStrStr = pathGlobe + '/product/description';
        			    var jsonStr = $.toJSON(productDetail);
        			    
        			    playSaving();
        			    
        			    $.ajax({ 
        			        type : 'POST',  
        			        contentType : 'application/json',  
        			        url : urlStrStr,  
        			        processData : false,  
        			        dataType : 'json',  
        			        data : jsonStr,  
        			        success : function(data) {  
        			        	checkCanPublish(data.productCan);
        			        	playSaved();
        			        	$("#productDescriptionLi").attr('class', 'publishchoosed');
        			        	$("#remainStepsSpan").html(data.stepsCount);
        			        },  
        			        error : function() {  
        			        	alert("network error---");
        			        }  
        			    }); 
        			}
        		}
        	}
        }
        
        function setProductDiscValue() {
        	var editor = CKEDITOR.instances.productOverviewDetailStr;
        	editor.insertHtml( productDetailStrGlobal );
        }
        
    </script>
</head>

<body>
    <form:form id="inputForm" modelAttribute="article" action="${ctx}/article/${action}" method="post" class="form-horizontal">
        <input type="hidden" name="articleId" value="${article.articleId}"/>
        <input type="hidden" name="articleName" value="${article.articleName}"/>
        <fieldset>
            <legend><small>管理</small></legend>
            <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">备注:</label>
                <div class="col-sm-10">
                    <input type="text" id="article_desc" name="articleDesc"  value="${article.articleDesc}" class="form-control "/>
                </div>
            </div>
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">内容:</label>
                <div class="col-sm-10">
                    <textarea id="article_content" name="articleContent" class="form-control" rows="10" cols="80">${article.articleContent}</textarea>
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

