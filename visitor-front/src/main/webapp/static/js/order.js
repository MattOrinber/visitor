function disableUnavailableDays(date) {
	var result = true;
	
	//determine is it is false---unselectable
	if (unavailDateList.length > 0) {
		var month = addLeadingZero(date.getMonth());
		var day = addLeadingZero(date.getDate());
		var year = date.getFullYear();
		var toCheck = year + '-' + month + '-' + day;
		if ($.inArray(toCheck, unavailDateList) != -1) {
			result = false;
		}
	}
	
	return result;
}

function doProductDateInit() {
	$("#toOrderStartDate").datepicker({
		dateFormat: "yy-mm-dd",
		beforeShowDay: disableUnavailableDays
	});
}