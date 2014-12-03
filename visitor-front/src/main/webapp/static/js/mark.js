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

function doFacebook() {
	$("._4z_f").addClass('spanFaceBook');
}
//_4z_f fwb

$(document).ready(function(){
	$("#signbtn").click(function(){
		$('.wrapwrapbox').show();
		$("#signup").show();
		doFacebook();
	});
	
	$("#logbtn").click(function(){
		$('.wrapwrapbox').show();
		$("#login").show();
		doFacebook();
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
	
	doImage();
});

$(window).scroll(function(){
	var targetTop = $(this).scrollTop();
	if (targetTop >= 520){
		$(".howtowork").hide();
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

//style="photo"
//class="width:28px;height:28px;border-radius:50%;display:inline-block;background:url(${userIconUrl}) no-repeat center center;float:left;"

//${loginName}

var itemPicUrlOne = 'class="width:28px;height:28px;border-radius:50%;display:inline-block;background:url(';
var itemPicUrlTwo = ') no-repeat center center;float:left;"';

var itemPicUrlSingle = 'style="photo"';

var afterLoginPartOne = '<a href="javascript:void(0);" class="name" id="name"><span ';
var afterLoginPartTwo = '></span><span class="peoplename">';
var afterLoginPartThree = '</span></a><span class="select" style="display: none;"><ul>' + 
	'<li><a href="#">Dashboard</a></li>' + 
	'<li><a href="">Inbox</a></li>' +
	'<li><a href="">Your Listings</a></li>' +
	'<li><a href="">Your Trips</a></li>' +
	'<li><a href="">Wish Lists</a></li>' +
	'<li><a href="">Edit Profile</a></li>' +
	'<li><a href="">Account</a></li>' +
	'<li><a href="">Log Out</a></li>' +
	'</ul></span>' +
	'<a href="javascript:void(0);" class="list">List Your Activities</a>' +
	'<a href="javascript:void(0);" class="mail"><span class="number">1</span></a>';

function doExtraStuff() {
	$("#name").mouseenter(function(){
		$('span.select').show();
	});
	$("span.select").mouseleave(function(){
		$(this).hide();
	});
	$("#login").hide();
}

function seeIfLoginBarDisplayed(data) {
	var userNameStr = data.userName;
    
    if (userNameStr == "--") {
    	console.log("not registered!");
    } else {
    	var userPicUrlStr = data.userPicUrl;
    	
    	var stringToDiv = '';
    	if (userPicUrlStr == "--") {
    		stringToDiv = afterLoginPartOne + itemPicUrlSingle + afterLoginPartTwo + userNameStr + afterLoginPartThree;
    	} else {
    		stringToDiv = afterLoginPartOne + itemPicUrlOne + userPicUrlStr + itemPicUrlTwo + afterLoginPartTwo + userNameStr + afterLoginPartThree;
    	}
    	
    	$("#loginBarToBeReplaced").html(stringToDiv);
    	
    	doExtraStuff();
    }
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
		        	var dataRes = "register result: " + data.result + "; resultDesc: " + data.resultDesc;
		            alert(dataRes);
		            
		            seeIfLoginBarDisplayed(data);
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
	        	var dataRes = "login result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            
	            seeIfLoginBarDisplayed(data);
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

