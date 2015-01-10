function loginUser() {
	var userNameStr = $.trim($("#usernameInput").val());
	var passwordStr = $.trim($("#passwordInput").val());
	
	if (userNameStr != "" && passwordStr != "") {
		var passwordMd5 = $.md5(passwordStr);
	    
	    var user = {};
	    var urlStrStr = pathGlobe + '/registerUser/loginback';
	    
	    user.emailStr = userNameStr;
	    user.passwordStr = passwordMd5;
	    var jsonStr = $.toJSON(user);
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	if (data.result == 0) {
	        		var hrefURL = pathGlobe +'/day/admin?type=0&p=1';
	        		window.location.href = hrefURL;
	        	} else {
	        		alert("login failed");
	        	}
	        },  
	        error : function() {  
	            alert('network err...');  
	        }  
	    }); 
	}
} 


function selectAdminTag(showContent,selfObj){
	var tag = document.getElementById("adminTags").getElementsByTagName("li");
	var taglength = tag.length;
	for(var i = 0; i < taglength; i ++) {
		tag[i].className = "";
	}
	
	selfObj.parentNode.className = "choosed";
	for(var i = 0; i < 8; i ++) {
		var j=document.getElementById("detail"+i);
		j.style.display = "none";
	}
	document.getElementById(showContent).style.display = "block";
}


function toUserSearchResult() {
	;
}


//container part
function toAddContainer() {
	;
}

function toDisplayContainerVal(id, name, type, value) {
	;
}

function updateContainerValue() {
	;
}


//floopy part
function addFloopy() {
	$("#detail1").hide();
	$("#floopyDetail").show();
}

function toUpdateFloopy(id, key, value, desc) {
	$("#floopyIdStr").val(id);
	$("#floopyKeyStr").val(key);
	$("#floopyValueStr").val(value);
	$("#floopyDescStr").val(desc);
	
	$("#floopyKeyStr").attr("disabled", "disabled");
	$("#detail1").hide();
	$("#floopyDetail").show();
}

function publishAllFloopy() {
    var urlStrStr = pathOri + '/floopy/publishAll';
    var jsonStr = '{"aa":"bb"}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	if (data.result == 0) {
        		alert("发布站点常量成功");
        	} else {
        		var dataRes = "publish floopyValue result: " + data.result + "; resultDesc: " + data.resultDesc;
        		alert(dataRes);
        	}
        },  
        error : function() {  
            alert('network error during publish all floopy values');  
        }  
    }); 
}

function updateFloopyValue() {
	var mark = $.trim($("#floopyIdStr").val());
	
	var keyStr = $.trim($("#floopyKeyStr").val());
	var valueStr = $.trim($("#floopyValueStr").val());
	var descStr = $.trim($("#floopyDescStr").val());
	
	if (keyStr != "" && valueStr != "" && descStr != "") {
		
		var floopyTemp = {};
		floopyTemp.keyStr = keyStr;
		floopyTemp.valueStr = valueStr;
		floopyTemp.descStr = descStr;
		
		var jsonStr = $.toJSON(floopyTemp);
		console.log(jsonStr);
		
		if (mark == "") {
			var urlStrStr = pathOri + '/floopy/addOne';
		    
		    $.ajax({ 
		        type : 'POST',  
		        contentType : 'application/json',  
		        url : urlStrStr,  
		        processData : false,  
		        dataType : 'json',  
		        data : jsonStr,  
		        success : function(data) {  
		        	if (data.result == 0) {
		        		alert("增加站点常量成功");
		        	} else {
		        		var dataRes = "add floopyValue result: " + data.result + "; resultDesc: " + data.resultDesc;
		        		alert(dataRes);
		        	}
		        },  
		        error : function() {  
		            alert('network error during adding one floopy value');  
		        }  
		    }); 
		} else {
			var urlStrStr = pathOri + '/floopy/updateOne';
		    
		    $.ajax({ 
		        type : 'POST',  
		        contentType : 'application/json',  
		        url : urlStrStr,  
		        processData : false,  
		        dataType : 'json',  
		        data : jsonStr,  
		        success : function(data) {  
		        	if (data.result == 0) {
		        		alert("更新站点常量成功");
		        	} else {
		        		var dataRes = "update floopyValue result: " + data.result + "; resultDesc: " + data.resultDesc;
		        		alert(dataRes);
		        	}
		        },  
		        error : function() {  
		            alert('network error during updating one floopy value');  
		        }  
		    }); 
		}
	} else {
		alert("请输入正确的值");
	}
}
