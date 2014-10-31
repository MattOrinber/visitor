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

function loginVisitor() {
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
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function saveVisitorMoreInfo() {
	;
}