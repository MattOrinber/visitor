var currentMonth = '2014-12-24';
var canBookMonth = '2014-12-25'; // 可以开始预订的日期， 从当前天延后一天 
var clickDateMonth = '';
var clickEndDateMonth = '';
var startDay = 0;
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
					var yearCurrent = $(this).data('year');
					var monthCurrent = addLeadingZero( $(this).data('month') );
					var dayCurrent = $(this).data('day');
					var dayCurrentInt = parseInt(dayCurrent);
					
					if (startDay < dayCurrentInt) {
						//var dateArray = [];
						for (var i = startDay; i <= dayCurrentInt; i ++) {
							var dayToSet = '' + i;
							var keyT = yearCurrent+'-'+monthCurrent+'-'+addLeadingZero(dayToSet);
							var needToClass = $(".responsive-calendar").find('[data-year="' + yearCurrent + '"][data-month="' + $(this).data('month') + '"][data-day="' + dayToSet + '"]').parent('.day');
							//dateArray[i-startDay] = keyT;
							//setDayClass(keyT);
							needToClass.addClass('active');
							//$(".responsive-calendar").responsiveCalendar('edit', {keyT:{"class": "active"}});
						}
						setDayClass(dateArray);
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