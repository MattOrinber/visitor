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

$(document).ready(function(){
	$("#signbtn").click(function(){
		$('.wrapwrapbox').show();
		$("#signup").show();
	});
	
	$("#logbtn").click(function(){
		$('.wrapwrapbox').show();
		$("#login").show();
	});
	
	$("#signupTologin").click(function(){
		$("#signup").hide();
		$("#login").show();
	});
	$("#loginTosignup").click(function(){
		$("#login").hide();
		$("#signup").show();
	});
	$(".wrapwrapbox").click(function(){
		$(this).hide();
		$("#signup").hide();
		$("#login").hide();
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