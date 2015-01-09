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
