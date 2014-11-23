// input form validator

// websocket part
// var url = "ws://172.18.100.66:61623/mybroker";
var url = "ws://192.168.1.106:61623/mybroker";
var __SPLIT__ = "---";

function connectToWS() {
	var ws_client = Stomp.client( url, "v11.stomp" );
	ws_client.connect("", "", function(){
		var destination = "/topic/test";
		ws_client.subscribe(destination, 
				function( message ) {
					alert( message );
				}, 
				{ priority: 9 } );
		ws_client.send(destination, {priority: 9}, "you are welcome!");
	});
}

function sendContent() {
	var ws_client = Stomp.client( url, "v11.stomp" );
	ws_client.connect("", "", function(){
		var destination = "/topic/test";
		ws_client.send(destination, {priority: 9}, "Not you!");
	});
}

$(document).ready(function(){
	CKEDITOR.replace( 'editorUserDetail', {
		coreStyles_bold: { element: 'b' },
		coreStyles_italic: { element: 'i' },

		fontSize_style: {
			element: 'font',
			attributes: { 'size': '#(size)' }
		}
	});
	
	// product
	CKEDITOR.replace( 'productOverviewDetailStr', {
		coreStyles_bold: { element: 'b' },
		coreStyles_italic: { element: 'i' },

		fontSize_style: {
			element: 'font',
			attributes: { 'size': '#(size)' }
		}
	});
	
	$("#productDatepicker").datepicker();
	
	connectToWS();
});


//--------------------------------------user part-----------------------

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

function saveVisitorMoreInfo(pathOri) {
	var form = $("#registerMoreInfo");
	form.validate();
	var idValidateForm = form.valid();
	if (idValidateForm) {
		var user = {};
		
		var emailStr = $("#emailStr").val();
		var passwordStr = $("#passwordStr").val();
	    
	    var passwordMd5 = $.md5(passwordStr);
		
		var firstNameStr = $("#firstNameStr").val();
		var lastNameStr = $("#lastNameStr").val();
		var genderStr = $("#gender").val();
		var phoneNumberStr = $("#phoneNumberStr").val();
		var birthDateStr = $("#birthDateStr").val();
		var emailRevStr = $("#emailRevStr").val();
		var addressStr = $("#addressStr").val();
		var descriptionStr = $("#descriptionStr").val();
		var schoolStr = $("#schoolStr").val();
		var workStr = $("#workStr").val();
		var timeZoneStr = $("#timeZoneStr").val();
		var languageSpokenSelect = "";
		
		var indexLSS = 0;
		$("input[name='languageSpokenSelectList']").each(function(){
			if ($(this).attr('checked')==true) {
				var valueT = $(this).val();
				if (indexLSS == 0) {
					languageSpokenSelect = languageSpokenSelect + valueT;
				} else {
					languageSpokenSelect = languageSpokenSelect + __SPLIT__ + valueT;
				}
				
				indexLSS ++;
			}
		});
		
		var emerNameStr = $("#emerNameStr").val();
		var emerPhoneStr = $("#emerPhoneStr").val();
		var emerEmailStr = $("#emerEmailStr").val();
		var emerRelationshipStr = $("#emerRelationshipStr").val();
		
		//var editorUserDetail = $("#editorUserDetail").html();
		var editor = CKEDITOR.instances.editorUserDetail;

		// Get editor contents
		// http://docs.ckeditor.com/#!/api/CKEDITOR.editor-method-getData
		var editorUserDetail = editor.getData();
		
		user.emailStr = emailStr;
		user.passwordStr = passwordMd5;
		user.firstNameStr = firstNameStr;
		user.lastNameStr = lastNameStr;
		user.genderStr = genderStr;
		user.phoneNumberStr = phoneNumberStr;
		user.birthDateStr = birthDateStr;
		user.emailRevStr = emailRevStr;
		user.addressStr = addressStr;
		user.descriptionStr = descriptionStr;
		user.schoolStr = schoolStr;
		user.workStr = workStr;
		user.timeZoneStr = timeZoneStr;
		user.languageSpokenSelect = languageSpokenSelect;
		user.emerNameStr = emerNameStr;
		user.emerPhoneStr = emerPhoneStr;
		user.emerEmailStr = emerEmailStr;
		user.emerRelationshipStr = emerRelationshipStr;
		user.editorUserDetail = editorUserDetail;
		
		
		var urlStrStr = pathOri + '/registerUser/postDetail';
		var jsonStr = $.toJSON(user);
		alert(jsonStr);
		
		$.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "detail post result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#registerMoreInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}




//---------------------------------product part---------------------------------

function createProduct(pathOri)
{
	var form = $("#productBasicInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		
		var userLoginEmailStr = $("#userLoginEmailStr").html();
		var userLoginTokenStr = $("#userLoginTokenStr").html();
		
		var product = {};
		var productHomeTypeStr = $("#productHomeTypeStr").val();
		var productRoomTypeStr = $("#productRoomTypeStr").val();
		var productAccomodatesStr = $("#productAccomodatesStr").val();
		var productCityStr = $("#productCityStr").val();
		
		product.productHomeTypeStr = productHomeTypeStr;
		product.productRoomTypeStr = productRoomTypeStr;
		product.productAccomodatesStr = productAccomodatesStr;
		product.productCityStr = productCityStr;
	    
	    var urlStrStr = pathOri + '/product/create?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
	    var jsonStr = $.toJSON(product);
		alert(jsonStr);
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "create product result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#productBasicInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	            
	            var pidDivVar = $("#productIdStr");
	            pidDivVar.html(data.productId);
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function saveProductDetails(pathOri)
{
	;
}
