var currentMonth = '2014-12-24';
var canBookMonth = '2014-12-25'; // 可以开始预订的日期， 从当前天延后一天 

//product operation date range
var clickDateMonth = '';
var clickEndDateMonth = '';
var availableType = '0'; //'0'-----可用; '1'-----不可用

var startDay = 0;
var endDay = 0;
var yearCurrent = '';
var monthCurrent = '';

var canDoSet = false;

function checkEndTime(startTime,endTime) {  // 查看点击的时间是不是可以预订
    var start=new Date(startTime.replace("-", "/").replace("-", "/"));  
    var end=new Date(endTime.replace("-", "/").replace("-", "/"));  
    if( end < start ) {  
        return false;  
    }  
    return true;  
}  

function setDayClass(timStr) {
	$(".responsive-calendar").responsiveCalendar('editDays', timStr);
}

function addLeadingZero(num) {
	if (num < 10) {
		return "0" + num;
	} else {
		return "" + num;
	}
}
function doCanlendarResponsive() {
	$(".responsive-calendar").responsiveCalendar({
		time: currentMonth,
		activateNonCurrentMonths: true,
		onDayClick: function(events) {
			var key;
			key = $(this).data('year')+'-'+addLeadingZero( $(this).data('month') )+'-'+addLeadingZero( $(this).data('day') );
			var ifAfter = checkEndTime(canBookMonth,key);
			if (ifAfter) {
				//alert("can be booked!");
				if (canDoSet) {
					clickEndDateMonth = key;
					canDoSet = false;
					
					//do popup dialog set
					popupDialog(this);
				} else {
					canDoSet = true;
					clickDateMonth = key;
					startDay = parseInt($(this).data('day'));
					var dateArray = [];
					dateArray[0] = clickDateMonth;
					//setDayClass(dateArray);
					$(this).parent().addClass('active');
					//$(".responsive-calendar").responsiveCalendar('edit', {clickDateMonth:{"class": "active"}});
				}
			} else {
				alert("can not be booked");
			}
		},
		onDayHover: function(events) {
			if (canDoSet) { 
				var key;
				key = $(this).data('year')+'-'+addLeadingZero( $(this).data('month') )+'-'+addLeadingZero( $(this).data('day') );
				var ifAfter = checkEndTime(clickDateMonth, key);
				if (ifAfter) {
					yearCurrent = $(this).data('year');
					monthCurrent = $(this).data('month');
					var dayCurrent = $(this).data('day');
					var dayCurrentInt = parseInt(dayCurrent);
					
					if (startDay < dayCurrentInt) {
						endDay = dayCurrentInt;
						//var dateArray = [];
						for (var i = startDay; i <= dayCurrentInt; i ++) {
							var dayToSet = '' + i;
							//var keyT = yearCurrent+'-'+monthCurrent+'-'+addLeadingZero(dayToSet);
							var needToClass = $(".responsive-calendar").find('[data-year="' + yearCurrent + '"][data-month="' + $(this).data('month') + '"][data-day="' + dayToSet + '"]').parent('.day');
							//dateArray[i-startDay] = keyT;
							//setDayClass(keyT);
							needToClass.addClass('active');
							//$(".responsive-calendar").responsiveCalendar('edit', {keyT:{"class": "active"}});
						}
						//setDayClass(dateArray);
					}
					//canDoSet = false;
					//alert("can be booked!");
				} else {
					//alert("can not be booked");
				}
			}
		}
	});
}

function popupDialog(currentNode) {
	var dayNode = $(currentNode);
	//var divNode = dayNode.parent();
	//var dialogNode = $("#originDialogPos");
	//divNode.html(dialogNode.html());
	//dialogNode.html('');
	
	var dayNodeX = dayNode.offset().left; 
	var dayNodeY = dayNode.offset().top;
	
	var dayNodeWidth = dayNode.outerWidth(true);
	var dayNodeHeight = dayNode.outerHeight(true);
	
	var dialogNode = $("#productOperationDialog");
	
	//var dialogNodeHeight = dialogNode.outerHeight(true);
	var dialogNodeWidth = dialogNode.outerWidth(true);
	
	var dialogNodeX = dayNodeX + dayNodeWidth/2 - dialogNodeWidth/2;
	var dialogNodeY = dayNodeY + dayNodeHeight;
	
	dialogNode.css("left", dialogNodeX + "px");
	dialogNode.css("top", dialogNodeY + "px");
	
	$("#productOperationStartDate").val(clickDateMonth);
	$("#productOperationEndDate").val(clickEndDateMonth);
	
	dialogNode.show();
}

function closeDialog() {
	$("#productOperationDialog").hide();
}

function setProductOperation() {
	var productOperation = {};
	var productIdStr = $("#productIdPageTemp").html();
	productOperation.productIdStr = productIdStr;
	productOperation.poTypeStr = availableType;
	productOperation.poStartDateStr = clickDateMonth;
	productOperation.poEndDateStr = clickEndDateMonth;
	
	if (availableType == '0') {
		var regExFloat = /^\d+(\.\d+)?$/;
	    var regExInt = /^[0-9]*[1-9][0-9]*$/;
	    var availPrice = $("#availablePrice").val();
	    if (regExFloat.test(availPrice) || regExInt.test(availPrice)) {
	    	var notes = $("#notesAvail").val();
	    	productOperation.poNoticeStr = notes;
	    	productOperation.poPricePerNightStr = availPrice;
	    } else {
	    	$("#availablePrice").val("");
			alert("please set a right price format");
	    }
	} else {
		var noteT = $("#notesUnavail").val();
		productOperation.poNoticeStr = noteT;
	}
	
	var urlStrStr = pathGlobe + '/product/saveOperation';
    var jsonStr = $.toJSON(productOperation);
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	setBackPrice(data.poType, data.poPrice);
        },  
        error : function() {  
            alert('Err...');  
        }  
    });
    
	closeDialog();
}

function setBackPrice(type, price) {
	for (var i = startDay; i <= endDay; i ++) {
		var dayToSet = '' + i;
		var needToClass = $(".responsive-calendar").find('[data-year="' + yearCurrent + '"][data-month="' + monthCurrent + '"][data-day="' + dayToSet + '"]').parent('.day');
		if (type == 0) {
			var htmlToInsert = '<span class="badge">$' + price + '</span>';
			needToClass.append(htmlToInsert);
		}
		needToClass.addClass("active");
		
	}
}

function setPopUpDialogCallback() {
	$("#btn_one").click(function(){
		$('#tabcontent1').show();
		$('#tabcontent2').hide();
		$(this).addClass("selected");
		$("#btn_two").removeClass("selected");
		availableType = '0';
	});
	
	$("#btn_two").click(function(){
		$('#tabcontent2').show();
		$('#tabcontent1').hide();
		$("#btn_one").removeClass("selected");
		$(this).addClass("selected");
		availableType = '1';
	});
}