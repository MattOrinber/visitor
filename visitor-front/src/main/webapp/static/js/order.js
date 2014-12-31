var unavailDateStrArray = null;
var orderIdReturnedFromExtraPriceSet = 0;

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
	
	var month = addLeadingZeroInOrder(date.getMonth() + 1);
	var day = addLeadingZeroInOrder(date.getDate());
	var year = date.getFullYear();
	var toCheck = year + '-' + month + '-' + day;
	
	//determine is it is false---unselectable
	if (unavailDateStrArray != null && unavailDateStrArray.length > 0) {
		if ($.inArray(toCheck, unavailDateStrArray) != -1) {
			result = false;
		}
	}
	
	var beginDate = new Date(toCheck.replace(/-/g,"/"));
	if(beginDate < myDate){
		result = false;
	}
	
	return [result];
}

//order generation call
function callOrderGeneration(dateText, dpInstance) {
	if (ifLogginIn == 0) {
		showLoginBarProposeDialog();
	} else {
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
	        	var productOrderId = data.orderId;
	        	var productPayOrderId = data.payOrderId;
	        	var priceTemp = data.totalPrice;
	        	
	        	$("#priceBasicSetPart").append('<span id="totalPriceBeforePay">total price: $ '+ priceTemp +'</span>');
	        	
	        	var hiddenServiceStr = $("#hiddenServicePricePart");
	        	if (hiddenServiceStr != null) {
	        		hiddenServiceStr.show();
	        	}
	        	orderIdReturnedFromExtraPriceSet = productOrderId;
	        	
	        	var payorderGenerationUrl = pathGlobe + "/order/expressCheckout/"+productOrderId+"/"+productPayOrderId;
	        	
	        	$("#toPayOrderButton").attr("href", payorderGenerationUrl);
	        },  
	        error : function() {  
	            alert('order generation error...');  
	        }  
	    });
	}
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
	if (orderIdReturnedFromExtraPriceSet > 0) {
		var orderIdStr = orderIdReturnedFromExtraPriceSet + "";
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
		        	$("#totalPriceBeforePay").html("total price: $ "+priceTemp);
		        	// paypal button change
	        	}
	        },  
	        error : function() {  
	            alert('order add service price error...');  
	        }  
	    });
	}
}
