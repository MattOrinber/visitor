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
	        		var hrefURL = pathGlobe +'/index';
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
