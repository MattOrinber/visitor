function doImage() {
	var aImg = $('#focus ul li');
	var iSize = aImg.size();
	var index = 0;
	//var t;
	$('#btnLeft').click(function(){
		index--;
		if ( index < 0 ) {
			index = iSize - 1;
		}
		change(index);
	});
	$('#btnRight').click(function(){
		index++;
		if(index > (iSize-1) ) {
			index = 0;
		}
		change(index);
	});
	
	function change(index){
		//aPage.removeClass('active');
		//aPage.eq(index).addClass('active');
		aImg.stop();
		aImg.eq(index).siblings().animate({
			opacity:0
		}, 1000);
		
		aImg.eq(index).animate({
			opacity:1
		},1000);
	}
	function autoshow() {
		index=index+1;
		if(index <= (iSize-1)){
			change(index);
		}else{
			index=0;
			change(index);
		}
	}
	int=setInterval(autoshow, 4000);
}

function doCanlendarPage() {
	$("#showfirst").click(function(){
		$('#first').show();
	});
	
	$("#hidefirst").click(function(){
		$('#first').hide();
	});
	
	$("#showsecond").click(function(){
		$('#second').show();
	});
	
	$("#hidesecond").click(function(){
		$('#second').hide();
	});
	
	$("#showthird").click(function(){
		$('#third').show();
	});
	
	$("#hidethird").click(function(){
		$('#third').hide();
	});
}

function doProductImageRound(){
	$(".img").mouseenter(function(){
		$(this).find(".btn").show();
	});
	$(".img").mouseleave(function(){
		$(this).find(".btn").hide();
	});
	$('.content .img a#btnLeft').click(function(){
		var currentImg = $(this).parent().find('[anime-status="1"]');
		currentImg.stop();
		currentImg.siblings().animate({
			opacity:0
		},100);
		currentImg.animate({
			opacity:1
		},100);
		currentImg.attr('anime-status','0');
		currentImg.siblings().attr('anime-status','1');
	});
}

function productBigImage() {
	$(".boxshade").hide();
	$("#bigImgshow,#bigImgshow1,#bigImgshow2").click(function(){
		$(".boxshade").show();
	});
	$("#closeit").click(function(){
		$(".boxshade").hide();
	});
}

$(document).ready(function(){
	$("#signbtn").click(function(){
		$('.wrapwrapbox').show();
		$("#signup").show();
	});
	
	$("#logbtn").click(function(){
		$('.wrapwrapbox').show();
		$("#login").show();
	});
	
	$("#signupTologin,#signupTologin1").click(function(){
		$("#signup").hide();
		$("#emailsignup").hide();
		$("#login").show();
	});
	$("#loginTosignup").click(function(){
		$("#login").hide();
		$("#emailsignup").hide();
		$("#signup").show();
	});
	$(".wrapwrapbox").click(function(){
		$(this).hide();
		$("#signup").hide();
		$("#login").hide();
		$("#emailsignup").hide();
	});
	$("#email_signup").click(function(){
		$("#signup").hide();
		$("#emailsignup").show();
	});
	
	$("#searchinput").click(function(){
		$(".choose_city").show();
	});
	
	$("#close").click(function(){
		$(".choose_city").hide();
	});
	
	$("#ScrolltoTop").click(function() {
		$('html, body').animate({
			scrollTop: 0
		}, "normal");
	});
	
	$(".destination li").click(function(){
		var htmlText = $(this).html();
		$("#searchinput").val(htmlText);
		$(this).parent().parent().hide();
	});
	
	//shade change
	$(".hover").mouseenter(function(){
		$(this).find(".shade").fadeIn();
	});
	
	$(".hover").mouseleave(function(){
		$(this).find(".shade").fadeOut();
	});
	
	//how to work
	$("#howtowork").click(function(){
		$(".howtowork").slideDown(800);
	});
	
	$("#hideit").click(function(){
		$(".howtowork").hide();
	});
	
	$("#name").mouseenter(function(){
		$('span.select').show();
	});
	
	$("span.select").mouseleave(function(){
		$(this).hide();
	});
	
	doImage();
	doCanlendarPage();
	doProductImageRound();
	productBigImage();
});

$(window).scroll(function(){
	var targetTop = $(this).scrollTop();
	if (targetTop >= 520){
		$(".howtowork").hide();
	}
	
	if (targetTop >= 520){
		$(".menu").addClass("fixedSubNav");
		$(".scrollbox").addClass("fixedbook");
	}else{
		$(".menu").removeClass("fixedSubNav");
		$(".scrollbox").removeClass("fixedbook");
	}
});

function doRegisterClean() {
	var passwordStr = $("#passwordStr").val();
	var cPasswordStr = $("#cPasswordStr").val();
	
	var emailStr = $("#emailStr").val();
	var firstNameStr = $("#firstNameStr").val();
	var lastNameStr = $("#lastNameStr").val();
	
	if ("Password" == passwordStr) {
		$("#passwordStr").val("");
	}
	if ("Confirm Password" == cPasswordStr) {
		$("#cPasswordStr").val("");
	}
	
	if ("Email Address" == emailStr) {
		$("#emailStr").val("");
	}
	
	if ("First name" == firstNameStr) {
		$("#firstNameStr").val("");
	}
	
	if ("Last name" == lastNameStr) {
		$("#lastNameStr").val("");
	}
}

function doLoginClean() {
	var loginEmailStr = $("#loginEmailStr").val();
	var loginPasswordStr = $("#loginPasswordStr").val();
	
	if ("Email Address" == loginEmailStr) {
		$("#loginEmailStr").val("");
	}
	if ("Password" == loginPasswordStr) {
		$("#loginPasswordStr").val("");
	}
}

// user token and replace part

function doExtraStuff() {
	$(".wrapwrapbox").hide();
	$("#signup").hide();
	$("#login").hide();
	$("#emailsignup").hide();
}

function seeIfLoginBarDisplayed(data, path) {
	var userNameStr = data.userName;
    
    if (userNameStr == "--") {
    	console.log("not registered!");
    } else {
    	var userPicUrlStr = data.userPicUrl;
    	var userEmailStr = data.userEmail;
    	var userTokenStr = data.token;
    	
    	if (userPicUrlStr == "--") {
    		$("#userIconUrlSpan").css("background", "url("+path+"/static/img/user_pic-50x50.png)");
    	} else {
    		$("#userIconUrlSpan").css("background", "url(" + userPicUrlStr + ")");
    	}
    	
    	$("#notLoginStatusBarPart").css("display", "none");
    	$("#loginStatusBarPart").css("display", "block");
    	$("#userNameSpan").html(userNameStr);
    	$.cookie('userEmail', userEmailStr, { expires: 7 });
    	$.cookie('userAccessToken', userTokenStr, { expires: 7 });
    	
    	doExtraStuff();
    }
}

function logoutBarDisplayed(data, path) {
	$("#userIconUrlSpan").css("background", "url("+path+"/static/img/user_pic-50x50.png)");
	$("#notLoginStatusBarPart").css("display", "block");
	$("#loginStatusBarPart").css("display", "none");
	$("#userNameSpan").html("");
	$.removeCookie('userEmail');
	$.removeCookie('userAccessToken');
}

function registerVisitor(pathOri)
{
	doRegisterClean();
	var form = $("#registerBasicInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		var passwordStr = $("#passwordStr").val();
		var cPasswordStr = $("#cPasswordStr").val();
		
		if (passwordStr == cPasswordStr) {
			var emailStr = $("#emailStr").val();
			var firstNameStr = $("#firstNameStr").val();
			var lastNameStr = $("#lastNameStr").val();
			
			var passwordMd5 = $.md5(passwordStr);
			
			var user = {};
			user.emailStr = emailStr;
			user.passwordStr = passwordMd5;
			user.firstNameStr = firstNameStr;
			user.lastNameStr = lastNameStr;
			var jsonStr = $.toJSON(user);
		    
		    var urlStrStr = pathOri + '/registerUser/register';
		    
		    $.ajax({ 
		        type : 'POST',  
		        contentType : 'application/json',  
		        url : urlStrStr,  
		        processData : false,  
		        dataType : 'json',  
		        data : jsonStr,  
		        success : function(data) {  
		        	//var dataRes = "register result: " + data.result + "; resultDesc: " + data.resultDesc;
		            //alert(dataRes);
		            
		            seeIfLoginBarDisplayed(data, pathOri);
		        },  
		        error : function() {  
		            alert('Err...');  
		        }  
		    }); 
		} else {
			var toSetNode = $("#cPasswordStr").parent();
			toSetNode.append("<p class=\"username\" color=\"red\">two passwrods not match</p>");
		}
	}
}

function loginVisitor(pathOri) {
	doLoginClean();
	var form = $("#loginBasicInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		var emailStr = $("#loginEmailStr").val();
		var passwordStr = $("#loginPasswordStr").val();
	    var passwordMd5 = $.md5(passwordStr);
	    
	    var user = {};
		user.emailStr = emailStr;
		user.passwordStr = passwordMd5;
	    
	    var urlStrStr = pathOri + '/registerUser/login';
	    var jsonStr = $.toJSON(user);
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	//var dataRes = "login result: " + data.result + "; resultDesc: " + data.resultDesc;
	            //alert(dataRes);
	            
	            seeIfLoginBarDisplayed(data, pathOri);
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function logoutVisitor(pathOri) {
	var emailStr = $.cookie('userEmail');
	var passwordStr = $.cookie('userAccessToken');
    
    var user = {};
	user.emailStr = emailStr;
	user.passwordStr = passwordStr;
    
    var urlStrStr = pathOri + '/registerUser/logout';
    var jsonStr = $.toJSON(user);
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	//var dataRes = "login result: " + data.result + "; resultDesc: " + data.resultDesc;
            //alert(dataRes);
            if (data.result == 0) {
            	logoutBarDisplayed(data, pathOri);
            }
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}

