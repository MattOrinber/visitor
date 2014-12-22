function saveUserDetailToBack() {
	var user = {};
	var needUpdate = false;
	
	var emailStr = $("#userEmailHidden").val();
	
	var firstNameStr = $.trim($("#firstNameStr").val());
	if (firstNameStr != null && firstNameStr != "") {
		user.emailStr = emailStr;
	}
	var lastNameStr = $.trim($("#lastNameStr").val());
	if (lastNameStr != null && lastNameStr != "") {
		user.lastNameStr = lastNameStr;
		needUpdate = true;
	}
	var genderStr = $("#gender").val();
	if (genderStr != null && genderStr != "" && genderStr != "0") {
		user.genderStr = genderStr;
		needUpdate = true;
	}
	
	var birthDateDay = $("#birthDateDay").val();
	var birthDateMonth = $("#birthDateMonth").val();
	var birthDateYear = $("#birthDateYear").val();
	
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
	
	var selfDescriptionStr = $.trim($("#selfDescriptionStr").val());
	if (selfDescriptionStr != null && selfDescriptionStr != "") {
		user.descriptionStr = selfDescriptionStr;
		needUpdate = true;
	}
	
	var workStr = $.trim($("#userWorkStr").val());
	if (workStr != null && workStr != "") {
		user.workStr = workStr;
		needUpdate = true;
	}
	
	var timeZoneStr = $("#timeZoneStr").val();
	if (timeZoneStr != null && timeZoneStr != "") {
		user.timeZoneStr = timeZoneStr;
		needUpdate = true;
	}
	
	if (needUpdate) {
		var urlStrStr = pathGlobe + '/updateUser/postUserDetail';
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
	
	$("#uploadUserPicForm").submit(function() {
    	
    	var picUploadUrl = pathGlobe + "/updateUser/usericon";
    	
        $("#uploadUserPicForm").ajaxSubmit({
        	type: "post",
        	url: picUploadUrl,
        	success: function (dataT) {
        		var data = $.secureEvalJSON(dataT);
        		if (data.result == 0) {
	        		var imageUrl =  data.imageUrl;
	        		$("#resultUserPicUpload").append("<span>" + imageUrl + "</span><br />");
        		} else {
        			alert(data.resultDesc);
        		}
            },
            error: function (msg) {
            	alert("image upload failed");
            }
        });
        return false;
    });
    $("#uploadUserPicForm").submit();
}

function contactHost(loginUserEmail, hostUserEmail) {
	var dialog = $("#contactHostDialog");
	$("#loginUserMailForMail").html(loginUserEmail);
	$("#hostUserMailForMail").html(hostUserEmail);
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
	;
}