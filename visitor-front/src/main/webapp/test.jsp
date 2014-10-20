<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
   String path = request.getContextPath();
   String basePath = request.getScheme() + "://" + request.getLocalAddr() + ":"+ request.getServerPort() + path + "/";
   basePath = path + "/";
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>OK Game User Center</title>

<style>
	*{margin:0;padding:0;font-family:"microsoft yahei";font-size:13px;}
	form{margin-left:20px;margin-top:20px;}
	form span{display:inline-block;width:65px;height:30px;}
	form input{width:160px;height:24px;border:1px solid #999;}
	form input.button{width:80px;height:30px;border-radius:5px;}
	form input.button:hover{font-weight:bold;}
	a{margin-left:20px;margin-top:10px;display:block;}
</style>

	<script src="<%=basePath%>js/jquery.js" type="text/javascript"></script>
	<script src="<%=basePath%>js/jquery.md5.js" type="text/javascript"></script>
	<script type="text/javascript">
	    var secKey = "35b207ce770952dcf5e2ca46dbcf4476";
	    
	    function moAction()
	    {
	    	//http://192.168.1.66:8085/oauth/token?client_id=unity-client&client_secret=unity&grant_type=refresh_token&scope=read,write&username=wum_132&password=wum_132&refresh_token=58ab58f1-c8d8-4799-b092-98e81584dfb6
	        var linkid = $("#linkid").val();
	        var mobile = $("#mobile").val();
	        var motime = $("#motime").val();
	        var mcc = $("#mcc").val();
	        var oprcode = $("#oprcode").val();
	        var shortcode = $("#shortcode").val();
	        var content = $("#content").val();
	        var fee = $("#fee").val();
	        var usershare = $("#usershare").val();
	        var dlr = $("#dlr").val();
	        
	        var preMd5Str = 'content=' + content + 
	        	'fee=' + fee + 
	        	'linkid=' + linkid + 
	        	'mcc=' + mcc + 
	        	'mobile=' + mobile + 
	        	'motime=' + motime + 
	        	'oprcode=' + oprcode + 
	        	'shortcode=' + shortcode + 
	        	'usershare=' + usershare + secKey;
	        
	        $("#preMd5").html(preMd5Str);
	        
	        var postMd5Str = $.md5(preMd5Str);
	        
	        $("#postMd5").html(postMd5Str);
	        
	        var queryStr = '?content=' + content + 
	        		'&fee=' + fee + 
	        		'&linkid=' + linkid + 
	        		'&mcc=' + mcc + 
	        		'&mobile=' + mobile + 
	        		'&motime=' + motime + 
	        		'&oprcode=' + oprcode + 
	        		'&shortcode=' + shortcode + 
	        		'&usershare=' + usershare + 
	        		'&sign=' + postMd5Str;
	        
	        var rTUrl = '<%=basePath%>mo' + queryStr;
	        
	        $.ajax({ 
	            type : 'POST',  
	            contentType : 'text/plain',  
	            url : rTUrl,  
	            processData : false,  
	            dataType : 'text',   
	            success : function(data) { 
	            	alert(data);
	            },  
	            error : function() {  
	                alert('Err...');  
	            }  
	        }); 
	    }
	    
	    function mrAction()
	    {
	    	//http://192.168.1.66:8085/oauth/token?client_id=unity-client&client_secret=unity&grant_type=refresh_token&scope=read,write&username=wum_132&password=wum_132&refresh_token=58ab58f1-c8d8-4799-b092-98e81584dfb6
	        var linkid = $("#linkid").val();
	        var mobile = $("#mobile").val();
	        var mrtime = $("#mrtime").val();
	        var mcc = $("#mcc").val();
	        var oprcode = $("#oprcode").val();
	        var shortcode = $("#shortcode").val();
	        var content = $("#content").val();
	        var fee = $("#fee").val();
	        var usershare = $("#usershare").val();
	        var dlr = $("#dlr").val();
	        
	        var preMd5Str = 'dlr=' + dlr + 
	        	'fee=' + fee + 
	        	'linkid=' + linkid + 
	        	'mcc=' + mcc + 
	        	'mobile=' + mobile + 
	        	'mrtime=' + mrtime + 
	        	'oprcode=' + oprcode + 
	        	'usershare=' + usershare + secKey;
	        
	        var postMd5Str = $.md5(preMd5Str);
	        
	        var queryStr = '?dlr=' + dlr + 
	        		'&fee=' + fee + 
	        		'&linkid=' + linkid + 
	        		'&mcc=' + mcc + 
	        		'&mobile=' + mobile + 
	        		'&mrtime=' + mrtime + 
	        		'&oprcode=' + oprcode + 
	        		'&usershare=' + usershare + 
	        		'&sign=' + postMd5Str;
	        
	        var rTUrl = '<%=basePath%>mr' + queryStr;
	        
	        $.ajax({ 
	            type : 'POST',  
	            contentType : 'text/plain',  
	            url : rTUrl,  
	            processData : false,  
	            dataType : 'text',   
	            success : function(data) { 
	            	alert(data);
	            },  
	            error : function() {  
	                alert('Err...');  
	            }  
	        }); 
	    }
	    
	    
	</script>

</head>
<body>
	<form id="boxInfo">
	    <span>linkid</span><input type="text" id="linkid" name="linkid" value="201403061111"/><br />
		<span>mobile</span><input type="text" id="mobile" name="mobile" value="84908906717"/><br />
	    <span>motime</span><input type="text" id="motime" name="motime" value="20140306132021"/><br />
		<span>mcc</span><input type="text" id="mcc" name="mcc" value="460"/><br />
		<span>oprcode</span><input type="text" id="oprcode" name="oprcode" value="46000"/><br />
		<span>shortcode</span><input type="text" id="shortcode" name="shortcode" value="8540"></input><br />
		<span>content</span><input type="text" id="content" name="content" value="pplay"></input><br />
		<span>fee</span><input type="text" id="fee" name="fee" value="5000.0"></input><br />
		<span>usershare</span><input type="text" id="usershare" name="usershare" value="0.75"></input><br />
		<span>dlr</span><input type="text" id="dlr" name="dlr" value="DELIVRD"></input><br />
		<span>mrtime</span><input type="text" id="mrtime" name="mrtime" value="20140306132221"/><br />
		
		<input class="button" type="button" value="MO" onclick="moAction();"/><br />
	    <input class="button" type="button" value="MR" onclick="mrAction();"/><br />
	</form>
	
	<div id="preMd5"></div>
	<div id="postMd5"></div>
</body>
</html>