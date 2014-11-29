// input form validator

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
