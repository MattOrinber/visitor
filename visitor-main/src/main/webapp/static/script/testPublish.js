// input form validator

function registerVisitor(pathOri)
{
	var form = $("#registerBasicInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		var emailStr = $("#emailStr").val();
		var passwordStr = $("#passwordStr").val();
	    
	    var passwordMd5 = $.md5(passwordStr);
	    
	    var urlStrStr = pathOri + '/registerUser/register/'+emailStr+'/'+passwordMd5;
	    var jsonStr = '{"aa":"bb"}';
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "register result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#registerBasicInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function loginVisitor(pathOri) {
	var form = $("#registerBasicInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		var emailStr = $("#emailStr").val();
		var passwordStr = $("#passwordStr").val();
	    
	    var passwordMd5 = $.md5(passwordStr);
	    
	    var urlStrStr = pathOri + '/registerUser/login/'+emailStr+'/'+passwordMd5;
	    var jsonStr = '{"aa":"bb"}';
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "register result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#registerBasicInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	            var tokenBoxVar = $("#userLoginTokenStr");
	            var emailBoxVar = $("#userLoginEmailStr");
	            tokenBoxVar.html(data.token);
	            emailBoxVar.html(data.userEmail);
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function publishTimeZone(pathOri)
{
	var userLoginEmailStr = $("#userLoginEmailStr").html();
	var userLoginTokenStr = $("#userLoginTokenStr").html();
	
    var urlStrStr = pathOri + '/publishinfo/timezone?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
    var jsonStr = '{"aa":"bb"}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "publish result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#publishBasicInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function publishLanuage(pathOri) 
{
	var userLoginEmailStr = $("#userLoginEmailStr").html();
	var userLoginTokenStr = $("#userLoginTokenStr").html();
    
    var urlStrStr = pathOri + '/publishinfo/language?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
    var jsonStr = '{"aa":"bb"}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "publish language result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#publishBasicInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function publishFloopyValues(pathOri) 
{
	var userLoginEmailStr = $("#userLoginEmailStr").html();
	var userLoginTokenStr = $("#userLoginTokenStr").html();
    
    var urlStrStr = pathOri + '/floopy/publishAll?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
    var jsonStr = '{"aa":"bb"}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "publish floopyValue result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#publishBasicInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}
