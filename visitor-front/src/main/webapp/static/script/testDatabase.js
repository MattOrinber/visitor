// input form validator
function generateVisitor()
{
}

function registerVisitor(pathOri)
{
	var form = $("#boxInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		var emailStr = $("#emailStr").val();
		var passwordStr = $("#passwordStr").val();
	    
	    var passwordMd5 = $.md5(passwordStr);
	    
	    var urlStrStr = pathOri + '/registerUser/register/'+emailStr+'/'+passwordMd5;
	    //var jsonStr = '{"aa":"bb"}';
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        //data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "register result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#boxInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function loginVisitor()
{
}

function bindEmailVisitor()
{
}

function updatePasswordVisitor()
{
}

function retrievePasswordVisitor()
{
}

function getTokenVisitor()
{
}

function verifyVisitor()
{
}

function refreshTokenVisitor()
{
}