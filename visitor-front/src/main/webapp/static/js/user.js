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
		var checkinDateStr = $("#checkinDate").val();
		var checkoutDateStr = $("#checkoutDate").val();
		var guestNumber = $("#guestNumber").val();
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

function replyEmail(uimIdStr, pidStr) {
	$("#uimIdInputHidden").val(uimIdStr);
	$("#productIdInputHidden").val(pidStr);
	
	var dialog = $("#replyVisitorDialog");
	$("#checkinDate").datepicker({dateFormat: "yy-mm-dd"});
	$("#checkoutDate").datepicker({dateFormat: "yy-mm-dd"});
	
	$('.wrapwrapbox').show();
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
		var checkinDateStr = $.trim($("#checkinDate").val());
		var checkoutDateStr = $.trim($("#checkoutDate").val());
		var guestNumber = $.trim($("#guestNumber").val());
		var contentStr = $.trim($("#mailContent").val());
		
		if (checkinDateStr == "") {
			checkinDateStr = "1970-01-01";
		}
		if (checkoutDateStr == "") {
			checkoutDateStr = "1970-01-01";
		}
		if (guestNumber == "") {
			guestNumber = "0";
		}
		
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