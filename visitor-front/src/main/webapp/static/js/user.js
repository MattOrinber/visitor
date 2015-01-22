var userDetailStrGlobal = "";

function setUserDiscValue() {
	var editor = CKEDITOR.instances.selfDescriptionStr;
	editor.insertHtml(userDetailStrGlobal);
}

function initUserDetailTextarea() {
	var node = $("#selfDescriptionStr");
	if (node != null) {
		userDetailStrGlobal = $("#userDetailOnPage").html();
		CKEDITOR.replace( 'selfDescriptionStr', {
			on: {
				instanceReady:setUserDiscValue
			},
			coreStyles_bold: { 
				element: 'b' 
			},
			coreStyles_italic: { 
				element: 'i' 
			},
	
			fontSize_style: {
				element: 'font',
				attributes: { 'size': '#(size)' }
			},
			width:760,
			height:250
		});
	}
}
function saveUserDetailToBack() {
	var user = {};
	var needUpdate = false;
	
	var firstNameStr = $.trim($("#firstNameUpdateStr").val());
	if (firstNameStr != null && firstNameStr != "") {
		user.firstNameStr = firstNameStr;
		needUpdate = true;
	}
	var lastNameStr = $.trim($("#lastNameUpdateStr").val());
	if (lastNameStr != null && lastNameStr != "") {
		user.lastNameStr = lastNameStr;
		needUpdate = true;
	}
	var genderStr = $.trim($("#gender").val());
	if (genderStr != null && genderStr != "" && genderStr != "0") {
		user.genderStr = genderStr;
		needUpdate = true;
	}
	
	var birthDateDay = $.trim($("#birthDateDay").val());
	var birthDateMonth = $.trim($("#birthDateMonth").val());
	var birthDateYear = $.trim($("#birthDateYear").val());
	
	if (birthDateDay != null && birthDateDay != "" &&
			birthDateMonth != null && birthDateMonth != "" &&
			birthDateYear != null && birthDateYear != ""){
		var birthDateStr = birthDateYear + "-" + birthDateMonth + "-" + birthDateDay;
		user.birthDateStr = birthDateStr;
		needUpdate = true;
	}
	
	var phoneNumberStr = $.trim($("#userPhoneNumStr").val());
	if (phoneNumberStr != null && phoneNumberStr != "") {
		user.phoneNumberStr = phoneNumberStr;
		needUpdate = true;
	}
	
	var userPalpalNumStr = $.trim($("#userPalpalNumStr").val());
	if (userPalpalNumStr != null && userPalpalNumStr != "") {
		user.userPalpalNumStr = userPalpalNumStr;
		needUpdate = true;
	}
	
	var addressStr = $.trim($("#userAddressStr").val());
	if (addressStr != null && addressStr != "") {
		user.addressStr = addressStr;
		needUpdate = true;
	}
	
	var editor = CKEDITOR.instances.selfDescriptionStr;
	var editorUserDesc = $.trim(editor.getData());
	
	var oriDescriptionStr = $.trim($("#userDetailOnPage").html());
	if (editorUserDesc != null && editorUserDesc != "" && editorUserDesc != oriDescriptionStr) {
		user.descriptionStr = editorUserDesc;
		needUpdate = true;
	}
	
	var workStr = $.trim($("#userWorkStr").val());
	if (workStr != null && workStr != "") {
		user.workStr = workStr;
		needUpdate = true;
	}
	
	var timeZoneStr = $.trim($("#timeZoneStr").val());
	if (timeZoneStr != null && timeZoneStr != "") {
		user.timeZoneStr = timeZoneStr;
		needUpdate = true;
	}
	
	if (needUpdate) {
		var urlStrStr = pathGlobe + '/updateUser/postUserDetail';
		var jsonStr = $.toJSON(user);
		//alert(jsonStr);
		
		$.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "user detail post result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function doUserImageUpload() {
	var picUploadUrl = pathGlobe + "/updateUser/usericon";
	
    $("#uploadUserPicForm").ajaxSubmit({
    	type: "post",
    	url: picUploadUrl,
    	success: function (dataT) {
    		var data = $.secureEvalJSON(dataT);
    		if (data.result == 0) {
        		var imageUrl =  data.imageUrl;
        		//$("#resultUserPicUpload").append("<span>" + imageUrl + "</span><br />");
        		$("#userPictureDisplay").attr("src", imageUrl);
    		} else {
    			alert(data.resultDesc);
    		}
        },
        error: function (msg) {
        	alert("image upload failed");
        }
    });
}

function contactHost(loginUserEmail) {
	var dialog = $("#contactHostDialog");
	$("#checkinDate").datepicker({dateFormat: "yy-mm-dd"});
	$("#checkoutDate").datepicker({dateFormat: "yy-mm-dd"});
	
	$('.wrapwrapbox').show();
	dialog.show();
}

function closeDialog() {
	$("#contactHostDialog").hide();
	$('.wrapwrapbox').hide();
}

function checkAndSend() {
	var productIDForUse = $.trim($("#productIDForUse").val());
	
	if (productIDForUse != "") {
		//var checkinDateStr = $("#checkinDate").val();
		//var checkoutDateStr = $("#checkoutDate").val();
		//var guestNumber = $("#guestNumber").val();
		var checkinDateStr = "1970-01-01";
		var checkoutDateStr = "1970-01-01";
		var guestNumber = "1";
		var contentStr = $.trim($("#mailContent").val());
		
		if (contentStr == "") {
			contentStr = "+";
		}
		
		var __SPLIT__ = "---";
		
		var contentFinal = checkinDateStr + __SPLIT__ +
						checkoutDateStr	+ __SPLIT__ +
						guestNumber + __SPLIT__ +
						contentStr;
		
		var uim = {};
		uim.productIdStr = productIDForUse;
		uim.contentStr = contentFinal;
		
		var urlStrStr = pathGlobe + '/product/saveInternalMail';
		var jsonStr = $.toJSON(uim);
		//alert(jsonStr);
		
		$.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	if (data.result == 0) {
	        		closeDialog();
	        	} else {
	        		alert(data.resultDesc);
	        	}
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function replyEmail(uimIdStr, pidStr, contentStr) {
	$("#uimIdInputHidden").val(uimIdStr);
	$("#productIdInputHidden").val(pidStr);
	$("#msgContent").html(contentStr);
	
	var dialog = $("#replyVisitorDialog");
	$("#checkinDate").datepicker({dateFormat: "yy-mm-dd"});
	$("#checkoutDate").datepicker({dateFormat: "yy-mm-dd"});
	
	$('.wrapwrapbox').show();
	$("#msgDivPart").show();
	$("#replyDivPart").show();
	dialog.show();
}

function doToMeListSelect(node) {
	var valueT = $(node).val();
	
	if (valueT == "0") {
		$("#repliedListDiv").hide();
		$("#unreadListDiv").show();
	} else {
		$("#unreadListDiv").hide();
		$("#repliedListDiv").show();
	}
}

function checkEmail(contentStr) {
	$("#msgContent").html(contentStr);
	
	var dialog = $("#replyVisitorDialog");
	
	$('.wrapwrapbox').show();
	$("#msgDivPart").show();
	$("#replyDivPart").hide();
	dialog.show();
}

function closeReplyEmail() {
	$("#replyVisitorDialog").hide();
	$('.wrapwrapbox').hide();
}

function replyEmailActual(uimIdStr, pidStr) {
	var uimIdStrToUse = $.trim($("#uimIdInputHidden").val());
	var productIDForUse = $.trim($("#productIdInputHidden").val());
	
	if (uimIdStrToUse != "" && productIDForUse != "") {
		//var checkinDateStr = $.trim($("#checkinDate").val());
		//var checkoutDateStr = $.trim($("#checkoutDate").val());
		//var guestNumber = $.trim($("#guestNumber").val());
		var checkinDateStr = "1970-01-01";
		var checkoutDateStr = "1970-01-01";
		var guestNumber = "1";
		var contentStr = $.trim($("#mailContent").val());
		
		if (contentStr != "") {
			var __SPLIT__ = "---";
			
			var contentFinal = checkinDateStr + __SPLIT__ +
							checkoutDateStr	+ __SPLIT__ +
							guestNumber + __SPLIT__ +
							contentStr;
			
			var uim = {};
			uim.productIdStr = productIDForUse;
			uim.contentStr = contentFinal;
			uim.uimIdStr = uimIdStrToUse;
			
			var urlStrStr = pathGlobe + '/product/replyInternalMail';
			var jsonStr = $.toJSON(uim);
			//alert(jsonStr);
			
			$.ajax({ 
		        type : 'POST',  
		        contentType : 'application/json',  
		        url : urlStrStr,  
		        processData : false,  
		        dataType : 'json',  
		        data : jsonStr,  
		        success : function(data) {  
		        	if (data.result == 0) {
		        		closeReplyEmail();
		        	} else {
		        		alert(data.resultDesc);
		        	}
		        },  
		        error : function() {  
		            alert('Err...');  
		        }  
		    }); 
		}
	}
}

function checkInBox() {
	var urlStrStr = pathGlobe + '/product/getMailCount';
	var jsonStr = '{"aa":"bb"}';
	//alert(jsonStr);
	
	$.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	if (data.result > 0) {
        		//display the inbox item count
        		$("#inboxItemCount").html(data.result);
        		$("#inboxItemCount").show();
        	} else if (data.result < 0){
        		if (inboxCheckPilot != null) {
        			clearInterval(inboxCheckPilot);
        		}
        	}
        },  
        error : function() {
        }  
    }); 
}

function selectInboxTag(showContent,selfObj) {
	var tag = document.getElementById("inboxTags").getElementsByTagName("li");
	var taglength = tag.length;
	for(var i = 0; i < taglength; i ++) {
		tag[i].className = "";
	}
	
	selfObj.parentNode.className = "selected";
	for(var i = 1; i < 3; i ++) {
		var j=document.getElementById("detail"+i);
		j.style.display = "none";
	}
	document.getElementById(showContent).style.display = "block";
}

function toCityProducts(event, ui) {
	var nodeVal = $.trim($("#tripListPartSuggest").val());
	if (nodeVal != null && nodeVal != "") {
		var toGoUrl = pathGlobe + "/day/city?c=" + nodeVal + "&o=0&p=1";
		window.location.href = toGoUrl;
	}
}

function initMyTripCityStr() {
	var node = $("#tripListPartSuggest");
	if (node != null) {
		node.autocomplete({
			source: cityArrayToGo,
			change: toCityProducts
		});
	}
}

function requestUserRetrievePassword() {
	$('.wrapwrapbox').hide();
	$("#login").hide();
	$("#resetBoxIdStr").show();
}

function closeResendPasswordPrompt() {
	$('#promptMessageIdStr').hide();
}

function sendResetPasswordLink() {
	var emailStr = $.trim($("#resetEmailLinkInputStr").val());
	
	if (emailStr != "") {
		var emailRegEx = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
		if (emailRegEx.test(emailStr)) {
			var user = {};
			user.emailStr = emailStr;
			
			var urlStrStr = pathGlobe + '/registerUser/retrievepassword';
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
		        		$("#resetBoxIdStr").hide();
		        		$("#resetEmailHasSentSpan").html(data.userEmail+'.');
		        		$("#promptMessageIdStr").fadeIn(300);
		        		setTimeout("$('#promptMessageIdStr').fadeOut(300)",30000);
		        	} else {
		        		alert(data.resultDesc);
		        	}
		        },  
		        error : function() {  
		            alert('network error when doing password reset');  
		        }  
		    }); 
		} else {
			alert("please input valid email address");
		}
	} else {
		alert("please input your email address");
	}
}

function checkAndResetPassword() {
	var emailStr = $.trim($("#mailInput").val());
	var tokenStr = $.trim($("#tokenInput").val());
	
	var newPassOneStr = $.trim($("#newPassOne").val());
	var newPassTwoStr = $.trim($("#newPassTwo").val());
	
	if (newPassOneStr != "" && newPassTwoStr != "") {
		if (newPassTwoStr == newPassOneStr) {
			var passwordMd5 = $.md5(newPassOneStr);
			
			var user = {};
			user.emailStr = emailStr;
			user.userTokenStr = tokenStr;
			user.newPassStr = passwordMd5;
			
			var urlStrStr = pathGlobe + '/registerUser/updateUserPassword';
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
		        		console.log("update success");
		        		var userEmailReturnedStr = data.userEmail;
		            	var userTokenReturnedStr = data.token;
		            	
		            	$.cookie('userEmail', userEmailReturnedStr, { expires: 7, path: '/' });
		            	$.cookie('userAccessToken', userTokenReturnedStr, { expires: 7, path: '/' });
		            	
		        		var toGoUrl = pathGlobe + "/index";
		        		window.location.href = toGoUrl;
		        	} else {
		        		alert(data.resultDesc);
		        	}
		        },  
		        error : function() {  
		            alert('network error when doing password reset');  
		        }  
		    }); 
		} else {
			alert("repeated password does not match the first one");
		}
	} else {
		alert("please input your new password");
	}
}

function editChangePassword() {
	var oldPassStr = $.trim($("#editChangePasswordOldStr").val());
	var newPassOneStr = $.trim($("#editChangePasswordOneStr").val());
	var newPassTwoStr = $.trim($("#editChangePasswordTwoStr").val());
	
	if (oldPassStr != "" && newPassOneStr != "" && newPassTwoStr != "") {
		if (newPassTwoStr == newPassOneStr) {
			var passwordMd5 = $.md5(newPassOneStr);
			var oldPasswordMd5 = $.md5(oldPassStr);
			
			var user = {};
			user.oldPassStr = oldPasswordMd5;
			user.newPassStr = passwordMd5;
			
			var urlStrStr = pathGlobe + '/updateUser/changeUserPassword';
			var jsonStr = $.toJSON(user);
			
			playSaving();
			
			$.ajax({ 
		        type : 'POST',  
		        contentType : 'application/json',  
		        url : urlStrStr,  
		        processData : false,  
		        dataType : 'json',  
		        data : jsonStr,  
		        success : function(data) {  
		        	if (data.result == 0) {
		        		console.log("change password success");
		        		playSaved();
		        	} else {
		        		alert(data.resultDesc);
		        	}
		        },  
		        error : function() {  
		            alert('network error when doing password change');  
		        }  
		    }); 
		} else {
			alert("new repeated password does not match the new first one");
		}
	} else {
		alert("please input your old password and new password");
	}
}