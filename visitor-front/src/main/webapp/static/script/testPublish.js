// input form validator

var emailPublishStr = "wum_132@163.com";
var passwordPublishStr = "e10adc3949ba59abbe56e057f20f883e";

var splitStr = "---";

function publishTimeZone(pathOri)
{
    var passwordMd5 = $.md5(emailPublishStr + splitStr + passwordPublishStr);
    
    var urlStrStr = pathOri + '/publishinfo/timezone?userEmail='+emailPublishStr+'&userPassword='+passwordMd5;
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
    var passwordMd5 = $.md5(emailPublishStr + splitStr + passwordPublishStr);
    
    var urlStrStr = pathOri + '/publishinfo/language?userEmail='+emailPublishStr+'&userPassword='+passwordMd5;
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
