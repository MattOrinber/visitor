//global value area
var productDetailStrGlobal = "";
var ifLogginIn = 0;

//init index page
function initIndex() {
	// index city search input
	$("#searchinput").click(function(){
		$(".choose_city").show();
	});
	
	$("#close").click(function(){
		$(".choose_city").hide();
	});
	
	$(".destination li").click(function(){
		var htmlText = $(this).children(":first").html();
		$("#searchinput").val(htmlText);
		$(this).parent().parent().hide();
	});
	// index how to work
	$("#howtowork").click(function(){
		$(".howtowork").slideDown(800);
	});
	
	$("#hideit").click(function(){
		$(".howtowork").hide();
	});
	//index shade change
	$(".hover").mouseenter(function(){
		$(this).find(".shade").fadeIn();
	});
	
	$(".hover").mouseleave(function(){
		$(this).find(".shade").fadeOut();
	});
	
	doImage();
}

//index page Image round play
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

//产品可用类型页面设置
function initCanlendarPage() { //product available type choose
	$("#showfirst").click(function(){
		$('#first').show();
		saveProductAvailableType("0");
	});
	
	$("#hidefirst").click(function(){
		$('#first').hide();
		saveProductAvailableType("5");
	});
	
	$("#showsecond").click(function(){
		$('#second').show();
		saveProductAvailableType("1");
	});
	
	$("#hidesecond").click(function(){
		$('#second').hide();
		saveProductAvailableType("5");
	});
	
	$("#showthird").click(function(){
		$('#third').show();
		saveProductAvailableType("2");
	});
	
	$("#hidethird").click(function(){
		$('#third').hide();
		saveProductAvailableType("5");
	});
	
	var availOri = $("#availTypeStr").html();
	if (availOri == "0") {
		$('#first').show();
	} else if (availOri == "1") {
		$('#second').show();
	} else if (availOri == "2") {
		$('#third').show();
	}
}

//product 页面小图动作效果，以后再加入
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

//product page initialize
function productBigImage() {
	$(".boxshade").hide();
	$("#bigImgshow2").click(function(){
		$(".boxshade").show();
	});
	$("#closeit").click(function(){
		$(".boxshade").hide();
	});
}

function setGlobalCurrency() {
	var globalCurrencyStr = $("#globalCurrencySet").val();
	$.cookie('globalCurrency', globalCurrencyStr, { expires: 7 });
}

function doCitySelect(node) {
	var city = $(node).val();
	var urlToGo = pathGlobe + '/day/city?c=' + city;
	
	window.location.href = urlToGo;
}

//产品详情
function setProductDiscValue() {
	var editor = CKEDITOR.instances.productOverviewDetailStr;
	editor.insertHtml( productDetailStrGlobal );
}

// product page editor change to ckEditor
function setProductDescEditor() {
	// product
	var node = $("#productDetailDESCInfoStr");
	if (node != null) {
		productDetailStrGlobal = $("#productDetailDESCInfoStr").attr("data-inner");
		CKEDITOR.replace( 'productOverviewDetailStr', {
			on: {
				blur: onProductDescAndTitle,
				instanceReady:setProductDiscValue
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
			width:800,
			height:250
		});
	}
}

//login bar and page bottom control
function initLoginBar() {
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
	$("#name").mouseenter(function(){
		$('span.select').show();
	});
	
	$("span.select").mouseleave(function(){
		$(this).hide();
	});
	
	
	//bottom back to top
	$("#ScrolltoTop").click(function() {
		$('html, body').animate({
			scrollTop: 0
		}, "normal");
	});
}


//price page init
function checkBox(){
	var i=document.getElementById("productExtraPriceCheckbox");
	var box = document.getElementById("productExtraPriceOption");
	if(i.checked == true){
	  box.style.display="block"; 
	}else{
	  box.style.display="none";
	}
}
function initPricePage() {
	//pricing page
	$(".closebutton").click(function(){
		$(this).hide();
	});
}


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
    	$.cookie('userEmail', userEmailStr, { expires: 7, path: '/' });
    	$.cookie('userAccessToken', userTokenStr, { expires: 7, path: '/' });
    	
    	doExtraStuff();
    }
}

function logoutBarDisplayed(data, path) {
	$("#userIconUrlSpan").css("background", "url("+path+"/static/img/user_pic-50x50.png)");
	$("#notLoginStatusBarPart").css("display", "block");
	$("#loginStatusBarPart").css("display", "none");
	$("#userNameSpan").html("");
	$.removeCookie('userEmail', { path: '/' });
	$.removeCookie('userAccessToken', { path: '/' });
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
		            ifLogginIn = 1;
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
	            startChecking();
	            ifLogginIn = 1;
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
            	
            	//clear the inbox check pilot
            	if (inboxCheckPilot != null) {
        			clearInterval(inboxCheckPilot);
        		}
            }
        },  
        error : function() {  
            alert('Err...');  
        }  
    }); 
}



//---------------list_space page info
function setHomeTypeBack() { //re-choose the home type
	$("#homeTypeChosen").hide();
	$("#inputvalHomeType").html('0');
	$("#proceedToGenerate").attr("href", "javascript:void(0);");
}

function homeTypeValueSet(valueT) { // choose home type
	var toDNode = $("#inputvalHomeType");
	toDNode.html(valueT);
	
	$("#homeTypeChosen").show();
	checkIfCanProceed();
}

function setRoomTypeBack() { //re-choose the room type
	$("#roomTypeChosen").hide();
	$("#inputvalRoomType").html(0);
	$("#proceedToGenerate").attr("href", "javascript:void(0);");
}

function roomTypeValueSet(valueT) { //choose room type
	var toDNode = $("#inputvalRoomType");
	toDNode.html(valueT);
	
	$("#roomTypeChosen").show();
	checkIfCanProceed();
}


function checkIfCanProceed() { // see if we can create product
	var htmlHomeType = $("#inputvalHomeType").html();
	var htmlRoomType = $("#inputvalRoomType").html();
	var accomodatesValue = $("#accomodatesValueD").val();
	var productCityInput = $("#productCityInput").val();
	var productCityInputStr = $.trim(productCityInput);
	
	if (htmlHomeType != "0" && 
			htmlRoomType != "0" && 
			accomodatesValue != "0" && 
			productCityInputStr != "" && 
			productCityInputStr != "Rome,Pairs...") {
		$("#proceedToGenerate").attr("href", "javascript:proposeToSignUp();");
	} else {
		$("#proceedToGenerate").attr("href", "javascript:void(0);");
	}
}

function proposeToSignUp() {
	if (ifLogginIn == 0) {
		showLoginBarProposeDialog();
	} else {
		createProduct();
	}
}

function showLoginBarProposeDialog() {
	$('.wrapwrapbox').show();
	$("#login").show();
}

//my lists
function toListing() {
	var url = pathGlobe + "/day/your-listing";
	window.location.href = url;
}


//inbox checking
var inboxCheckPilot = null;

function startChecking() {
	var ifLoggedIn = $("#loginStatusBarPart").is(":visible");
	if (ifLoggedIn) {
		inboxCheckPilot = setInterval("checkInBox()", 10000); //every 10 seconds do a check
	}
}
