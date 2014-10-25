function generateVisitor()
{
    var username = $("#username").val();
    
    var jsonStr = '{"merchantId":"","gameId":"","userName":"","passWord":"","phoneNumber":"","email":"","newPass":"","authCode":""}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : '<%=basePath%>user/generateUser',  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "user name: " + data.username;
            alert(dataRes);
            var userNameT = data.username;
            var boxVar = $("#username");
            boxVar.val(userNameT);
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function registerVisitor()
{
	var merchantId = $("#merchantId").val();
	var gameId = $("#gameId").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var phoneNum = $("#phoneNumberInput").val();
    
    var passwordMd5 = $.md5(password);
    
    var jsonStr = '{"merchantId":"'+merchantId+'","gameId":"'+gameId+'","userName":"'+username+'","passWord":"'+passwordMd5+'","phoneNumber":"'+phoneNum+'","email":"","newPass":"","authCode":""}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : '<%=basePath%>user/register',  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
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

function loginVisitor()
{
	var merchantId = $("#merchantId").val();
	var gameId = $("#gameId").val();
    var username = $("#username").val();
    var password = $("#password").val();
    
    var passwordMd5 = $.md5(password);
    
    var jsonStr = '{"merchantId":"'+merchantId+'","gameId":"'+gameId+'","userName":"'+username+'","passWord":"'+passwordMd5+'","phoneNumber":"","email":"","newPass":"","authCode":""}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : '<%=basePath%>user/login',  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "login result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#boxInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function bindEmailVisitor()
{
	var merchantId = $("#merchantId").val();
	var gameId = $("#gameId").val();
    var username = $("#username").val();
    var password = $("#password").val();
    
    var passwordMd5 = $.md5(password);
    
    var emailStr = $("#emailAddress").val();
    
    var jsonStr = '{"merchantId":"'+merchantId+'","gameId":"'+gameId+'","userName":"'+username+'","passWord":"'+passwordMd5+'","phoneNumber":"","email":"'+emailStr+'","newPass":"","authCode":""}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : '<%=basePath%>user/bindemail',  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "bind email result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#boxInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function updatePasswordVisitor()
{
	var merchantId = $("#merchantId").val();
	var gameId = $("#gameId").val();
    var username = $("#username").val();
    var password = $("#password").val();
    var passwordMd5 = $.md5(password);
    
    var newpassword = $("#newpass").val();
    var newpasswordMd5 = $.md5(newpassword);
    
    var jsonStr = '{"merchantId":"'+merchantId+'","gameId":"'+gameId+'","userName":"'+username+'","passWord":"'+passwordMd5+'","phoneNumber":"","email":"","newPass":"'+newpasswordMd5+'","authCode":""}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : '<%=basePath%>user/updatepassword',  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "update password result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#boxInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function retrievePasswordVisitor()
{
    var username = $("#username").val();
    var emailStr = $("#emailAddress").val();
    
    var jsonStr = '{"merchantId":"","gameId":"","userName":"'+username+'","passWord":"","phoneNumber":"","email":"'+emailStr+'","newPass":"","authCode":""}';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : '<%=basePath%>user/retrievepassword',  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var dataRes = "update password result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#boxInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function getTokenVisitor()
{
	//http://192.168.1.66:8085/oauth/token?client_id=unity-client&client_secret=unity&grant_type=password&scope=read,write&username=wum_132&password=wum_132
    var username = $("#username").val();
    var password = $("#password").val();
    
    var passwordMd5 = $.md5(password);
    
    var jsonStr = '{"merchantId":"","gameId":"","userName":"'+username+'","passWord":"'+passwordMd5+'","phoneNumber":"","email":"","newPass":"","authCode":""}';
    var getUrl = '<%=basePath%>token/access';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : getUrl,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) { 
        	var aToken = data.access_token;
        	var rToken = data.refresh_token;
            var boxVar = $("#boxInfo");
            boxVar.append('<div id="refreshToken">'+rToken+'</div>');
            boxVar.append('<div id="accessToken">'+aToken+'</div>');
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function verifyVisitor()
{
	var username = $("#username").val();
    var accessToken = $("#accessToken").html();
    
    var jsonStr = '{"merchantId":"","gameId":"","userName":"'+username+'","passWord":"","phoneNumber":"","email":"","newPass":"","authCode":"'+accessToken+'"}';
    var verifyUrl = '<%=basePath%>token/verify';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',
        url : verifyUrl,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr, 
        success : function(data) { 
        	var dataRes = "verify result: " + data.result + "; resultDesc: " + data.resultDesc;
            alert(dataRes);
            var boxVar = $("#boxInfo");
            boxVar.append("<p>"+dataRes+"</p>");
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

function refreshTokenVisitor()
{
	//http://192.168.1.66:8085/oauth/token?client_id=unity-client&client_secret=unity&grant_type=refresh_token&scope=read,write&username=wum_132&password=wum_132&refresh_token=58ab58f1-c8d8-4799-b092-98e81584dfb6
    var username = $("#username").val();
    var password = $("#password").val();
    
    var passwordMd5 = $.md5(password);
    
    var refreshT = $("#refreshToken").html();
    var jsonStr = '{"merchantId":"","gameId":"","userName":"'+username+'","passWord":"'+passwordMd5+'","phoneNumber":"","email":"","newPass":"","authCode":"'+refreshT+'"}';
    
    var rTUrl = '<%=basePath%>token/refresh';
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : rTUrl,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) { 
        	var aToken = data.access_token;
        	var rToken = data.refresh_token;
            var rTokenDiv = $("#refreshToken");
            var aTokenDiv = $("#accessToken");
            aTokenDiv.html(aToken);
            rTokenDiv.html(rToken);
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}