var unavailDateStrArray = null;

function addLeadingZeroInOrder(num) {
	if (num < 10) {
		return "0" + num;
	} else {
		return "" + num;
	}
}

function disableUnavailableDays(date) {
	var result = true;
	var myDate = new Date();
	
	//determine is it is false---unselectable
	if (unavailDateStrArray != null && unavailDateStrArray.length > 0) {
		var month = addLeadingZeroInOrder(date.getMonth() + 1);
		var day = addLeadingZeroInOrder(date.getDate());
		var year = date.getFullYear();
		var toCheck = year + '-' + month + '-' + day;
		if ($.inArray(toCheck, unavailDateStrArray) != -1) {
			result = false;
		} else {
			var beginDate = new Date(toCheck.replace(/-/g,"/"));
			if(beginDate < myDate){
				result = false;
			}
		}
	}
	
	return [result];
}

//order generation call
function callOrderGeneration(dateText, dpInstance) {
	var pidStr = $("#productIDForUse").val();
	var startDateStr = dateText;
	var endDateStr = dateText;
	
	var buyTemp = {};
	buyTemp.productIdStr = pidStr;
	buyTemp.startDate = startDateStr;
	buyTemp.endDate = endDateStr;
	
	var urlStrStr = pathGlobe + '/order/calcTotalPrice';
    var jsonStr = $.toJSON(buyTemp);
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var productId = data.productId;
        	var productOrderId = data.orderId;
        	var priceTemp = data.poPrice;
        	
        	$("#priceBasicSetPart").append("<span>basic price: $ "+ priceTemp +"</span>");
        	var payorderGenerationUrl = pathGlobe + "/order/toPayOrder/"+productId+"/"+productOrderId;
        	
        	$("#toPayOrderButton").attr("href", payorderGenerationUrl);
        },  
        error : function() {  
            alert('order generation error...');  
        }  
    });
}

function doProductDateInit() {
	if(unavailDateList != "") {
		unavailDateStrArray = unavailDateList;
	}
	$("#toOrderStartDate").datepicker({
		dateFormat: "yy-mm-dd",
		beforeShowDay: disableUnavailableDays,
		onSelect: callOrderGeneration
	});
}

//pay order part
function addServicePrice(node) {
	var orderIdStr = $("#orderIDForUse").val();
	var priceSetId = $(node).attr("data-key");
	
	var buyTemp = {};
	buyTemp.orderIdStr = orderIdStr;
	buyTemp.priceIdStr = priceSetId;
	
	var urlStrStr = pathGlobe + '/order/calcTotalPrice';
    var jsonStr = $.toJSON(buyTemp);
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	var result = data.result;
        	if (result == 0) {
	        	var priceTemp = data.poPrice;
	        	$("#finalPriceSetToChange").html("$ "+priceTemp);
	        	// paypal button change
	        	
        	}
        },  
        error : function() {  
            alert('order add service price error...');  
        }  
    });
}
