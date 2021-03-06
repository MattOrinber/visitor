//---------------------------------product part---------------------------------
var ifCanUpdate = false;

function setifCanUpdate() {
	ifCanUpdate = true;
}

function setIfPriceCanUpdate(node) {
	ifCanUpdate = true;
	//$("#pricePageCurrencyStr").html($(node).val());
}

function createProduct()
{	
	var product = {};
	
	var productHomeTypeStr = 'Apartment';
	var productRoomTypeStr = 'Entire home/apt';
	var productAccomodatesStr = '5';
	var productAvailableTypeStr = '0';
	var productCityStr = $("#productCityInput").val();
	
	product.productHomeTypeStr = productHomeTypeStr;
	product.productRoomTypeStr = productRoomTypeStr;
	product.productAccomodatesStr = productAccomodatesStr;
	product.productCityStr = productCityStr;
	product.productAvailableTypeStr = productAvailableTypeStr;
    
    var urlStrStr = pathGlobe + '/product/create';
    var jsonStr = $.toJSON(product);
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {
        	$.cookie('currentProductID', data.productId, { expires: 7 });
        	var redirectStr = pathGlobe + '/day/pricing?pid=' + data.productId;
        	window.location.href = redirectStr;
        },  
        error : function() {  
        	alert("network error---");
        }  
    }); 
}


function publishProduct() {
	var productDetail = {};
	var productIdStr = $("#productIdPageTemp").html();
	
	productDetail.productIdStr = productIdStr;
	
    var urlStrStr = pathGlobe + '/product/publishproduct';
    var jsonStr = $.toJSON(productDetail);
    
    playSaving();
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	playSaved();
        	var htmlToGo = '<a href="javascript:void(0);" class="dot"></a><select><option selected="selected">Unlisted</option><option>listed</option></select>';
        	$("#placeToPublishDiv").html(htmlToGo);
        },  
        error : function() { 
        	alert("network error---");
        }  
    }); 
}

function checkCanPublish(ifCan) {
	if (ifCan == 1) {
		$("#placeToPublishDiv").html('<a href="javascript:publishProduct();" style="width: 150px;text-align: center;height: 35px;display: inline-block;background:#ff5a5f;border:1px solid #ff5a5f;border-bottom-color: #e00007;color: #fff;line-height: 35px;border-radius: 3px;">List Space</a>');
		return 1;
	} else {
		return 0;
	}
}

function playSaving() {
	$("#dispearText_saving").show().animate({opacity:0},2000); 
}

function playSaved() {
	$("#dispearText_saving").hide();
	$("#dispearText_saved").show().animate({opacity:0},2000);
}

function saveProductAvailableType(availType) {
	if (ifCanUpdate) {
		var productDetail = {};
		var productIdStr = $("#productIdPageTemp").html();
		var productAvailableTypeStr = availType;
		
		productDetail.productIdStr = productIdStr;
		productDetail.productAvailableTypeStr = productAvailableTypeStr;
	    
	    var urlStrStr = pathGlobe + '/product/availtype';
	    var jsonStr = $.toJSON(productDetail);
	    
	    playSaving();
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	checkCanPublish(data.productCan);
	        	playSaved();
	        	$("#productCalendarLi").attr('class', 'publishchoosed');
	        	$("#remainStepsSpan").html(data.stepsCount);
	        },  
	        error : function() { 
	        	alert("network error---");
	        }  
	    }); 
	}
}

function saveProductPriceSetting() {
	if (ifCanUpdate) {
		var regExFloat = /^\d+(\.\d+)?$/;
	    var regExInt = /^[0-9]*[1-9][0-9]*$/;
		var productBasePriceValue = $.trim($("#productBaseCurrencyValue").val());
		
		if (productBasePriceValue != "") {
			if (regExFloat.test(productBasePriceValue) || regExInt.test(productBasePriceValue)) {
				var productDetail = {};
				var productIdStr = $("#productIdPageTemp").html();
				var productCurrencyChoose = $("#productCurrencyChoose").val();
				
				productDetail.productIdStr = productIdStr;
				productDetail.productCurrencyStr = productCurrencyChoose;
				productDetail.productBasepriceStr = productBasePriceValue;
			    
			    var urlStrStr = pathGlobe + '/product/pricing';
			    var jsonStr = $.toJSON(productDetail);
			    
			    playSaving();
			    
			    $.ajax({ 
			        type : 'POST',  
			        contentType : 'application/json',  
			        url : urlStrStr,  
			        processData : false,  
			        dataType : 'json',  
			        data : jsonStr,  
			        success : function(data) {  
			        	checkCanPublish(data.productCan);
			        	playSaved();
			        	$("#productPriceLi").attr('class', 'publishchoosed');
			        	$("#remainStepsSpan").html(data.stepsCount);
			        },  
			        error : function() {  
			        	alert("network error---");
			        }  
			    }); 
			} else {
				$("#productBaseCurrencyValue").val("");
				alert("please set a right price format");
			}
		}
	}
}

function onProductDescAndTitle() {
	if (ifCanUpdate) {
		var titleStr = $.trim($("#productTitle").val());
		if (titleStr != "") {
			var editor = CKEDITOR.instances.productOverviewDetailStr;
			var editorProductDesc = $.trim(editor.getData());
			
			var originDescStr = $("#productDetailDESCInfoStr").attr("data-inner");
			if (editorProductDesc != "" && editorProductDesc != originDescStr) {
				var productDetail = {};
				var productIdStr = $("#productIdPageTemp").html();
				
				productDetail.productIdStr = productIdStr;
				productDetail.productOverviewTitleStr = titleStr;
				productDetail.productOverviewDetailStr = editorProductDesc;
			    
			    var urlStrStr = pathGlobe + '/product/description';
			    var jsonStr = $.toJSON(productDetail);
			    
			    playSaving();
			    
			    $.ajax({ 
			        type : 'POST',  
			        contentType : 'application/json',  
			        url : urlStrStr,  
			        processData : false,  
			        dataType : 'json',  
			        data : jsonStr,  
			        success : function(data) {  
			        	checkCanPublish(data.productCan);
			        	playSaved();
			        	$("#productDescriptionLi").attr('class', 'publishchoosed');
			        	$("#remainStepsSpan").html(data.stepsCount);
			        },  
			        error : function() {  
			        	alert("network error---");
			        }  
			    }); 
			}
		}
	}
}

function productAddressUpdate() {
	if (ifCanUpdate) {
		var inputNodeValue = $.trim($("#searchCityInput").val());
		
		if (inputNodeValue != "") {
			var productAddress = {};
			var productIdStr = $("#productIdPageTemp").html();
			
			productAddress.productIdStr = productIdStr;
			productAddress.productAddressDetailStr = inputNodeValue;
		    
		    var urlStrStr = pathGlobe + '/product/address';
		    var jsonStr = $.toJSON(productAddress);
		    
		    playSaving();
		    
		    $.ajax({ 
		        type : 'POST',  
		        contentType : 'application/json',  
		        url : urlStrStr,  
		        processData : false,  
		        dataType : 'json',  
		        data : jsonStr,  
		        success : function(data) {  
		        	checkCanPublish(data.productCan);
		        	playSaved();
		        },  
		        error : function() { 
		        	alert("network error---");
		        }  
		    }); 
		}
	}
}

function setProductCancellationPolicy() {
	var productIdStr = $("#productIdPageTemp").html();
	var productCancellationPolicyString = $("#productCancellationPolicyStr").val();
	
	if (productCancellationPolicyString != '0') {
		var productDetail = {};
		productDetail.productIdStr = productIdStr;
		productDetail.productCancellationPolicyStr = productCancellationPolicyString;
	    
	    var urlStrStr = pathGlobe + '/product/cancellationpolicy';
	    var jsonStr = $.toJSON(productDetail);
	    
	    playSaving();
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	checkCanPublish(data.productCan);
	        	playSaved();
	        	$("#productTermsLi").attr('class', 'publishchoosed');
	        	$("#remainStepsSpan").html(data.stepsCount);
	        },  
	        error : function() {  
	        	alert("network error---");
	        }  
	    });
	}
}

function saveProductDetails(pathOri)
{
	var form = $("#productMoreInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		
		var userLoginEmailStr = $("#userLoginEmailStr").html();
		var userLoginTokenStr = $("#userLoginTokenStr").html();
		
		var productDetail = {};
		var productIdStr = $("#productIdStr").html();
		var productAvailableTypeStr = $("#productAvailableType").val();
		var productCurrencyStr = $("#productCurrency").val();
		var productBasepriceStr = $("#productBasepriceStr").val();
		var productOverviewTitleStr = $("#productOverviewTitleStr").val();
		
		var editor = CKEDITOR.instances.productOverviewDetailStr;
		var productOverviewDetailStr = editor.getData();
		
		var amenitiesMostCommonSelected = "";
		var indexLSS = 0;
		$("input[name='amenitiesMostCommonList']").each(function(){
			if ($(this).attr('checked')==true) {
				var valueT = $(this).val();
				if (indexLSS == 0) {
					amenitiesMostCommonSelected = amenitiesMostCommonSelected + valueT;
				} else {
					amenitiesMostCommonSelected = amenitiesMostCommonSelected + __SPLIT__ + valueT;
				}
				
				indexLSS ++;
			}
		});
		
		var amenitiesExtrasSelected = "";
		indexLSS = 0;
		$("input[name='amenitiesExtrasList']").each(function(){
			if ($(this).attr('checked')==true) {
				var valueT = $(this).val();
				if (indexLSS == 0) {
					amenitiesExtrasSelected = amenitiesExtrasSelected + valueT;
				} else {
					amenitiesExtrasSelected = amenitiesExtrasSelected + __SPLIT__ + valueT;
				}
				
				indexLSS ++;
			}
		});
		
		var amenitiesSpecialFeaturesSelected = "";
		indexLSS = 0;
		$("input[name='amenitiesSpecialFeaturesList']").each(function(){
			if ($(this).attr('checked')==true) {
				var valueT = $(this).val();
				if (indexLSS == 0) {
					amenitiesSpecialFeaturesSelected = amenitiesSpecialFeaturesSelected + valueT;
				} else {
					amenitiesSpecialFeaturesSelected = amenitiesSpecialFeaturesSelected + __SPLIT__ + valueT;
				}
				
				indexLSS ++;
			}
		});
		
		var amenitiesHomeSafetySelected = "";
		indexLSS = 0;
		$("input[name='amenitiesHomeSafetyList']").each(function(){
			if ($(this).attr('checked')==true) {
				var valueT = $(this).val();
				if (indexLSS == 0) {
					amenitiesHomeSafetySelected = amenitiesHomeSafetySelected + valueT;
				} else {
					amenitiesHomeSafetySelected = amenitiesHomeSafetySelected + __SPLIT__ + valueT;
				}
				
				indexLSS ++;
			}
		});
		
		var productBedroomNumStr = $("#productBedroomNum").val();
		var productBedsNumStr = $("#productBedsNum").val();
		var productBathroomNumStr = $("#productBathroomNum").val();
		var productPricePerWeekStr = $("#productPricePerWeekStr").val();
		var productPricePerMonthStr = $("#productPricePerMonthStr").val();
		var minStayStr = $("#minStayStr").val();
		var maxStayStr = $("#maxStayStr").val();
		var productCheckinAfterStr = $("#productCheckinAfterStr").val();
		var productCheckoutBeforeStr = $("#productCheckoutBeforeStr").val();
		var productCancellationPolicyStr = $("#productCancellationPolicyStr").val();
		
		var productExtraInfoSpaceStr = $("#productExtraInfoSpaceStr").val();
		var productExtraInfoGuestAccessStr = $("#productExtraInfoGuestAccessStr").val();
		var productExtraInfoGuestInteractionStr = $("#productExtraInfoGuestInteractionStr").val();
		var productExtraInfoNeighborhoodStr = $("#productExtraInfoNeighborhoodStr").val();
		var productExtraInfoTransitStr = $("#productExtraInfoTransitStr").val();
		var productExtraInfoOtherNoteStr = $("#productExtraInfoOtherNoteStr").val();
		var productExtraInfoHouseRuleStr = $("#productExtraInfoHouseRuleStr").val();
		var productExtraInfoHouseManualStr = $("#productExtraInfoHouseManualStr").val();
		var productExtraInfoDirectionStr = $("#productExtraInfoDirectionStr").val();
		
		productDetail.productIdStr = productIdStr;
		productDetail.productAvailableTypeStr = productAvailableTypeStr;
		productDetail.productCurrencyStr = productCurrencyStr;
		productDetail.productBasepriceStr = productBasepriceStr;
		productDetail.productOverviewTitleStr = productOverviewTitleStr;
		productDetail.productOverviewDetailStr = productOverviewDetailStr;
		productDetail.amenitiesMostCommonSelected = amenitiesMostCommonSelected;
		productDetail.amenitiesExtrasSelected = amenitiesExtrasSelected;
		productDetail.amenitiesSpecialFeaturesSelected = amenitiesSpecialFeaturesSelected;
		productDetail.amenitiesHomeSafetySelected = amenitiesHomeSafetySelected;
		productDetail.productBedroomNumStr = productBedroomNumStr;
		productDetail.productBedsNumStr = productBedsNumStr;
		productDetail.productBathroomNumStr = productBathroomNumStr;
		productDetail.productPricePerWeekStr = productPricePerWeekStr;
		productDetail.productPricePerMonthStr = productPricePerMonthStr;
		productDetail.minStayStr = minStayStr;
		productDetail.maxStayStr = maxStayStr;
		productDetail.productCheckinAfterStr = productCheckinAfterStr;
		productDetail.productCheckoutBeforeStr = productCheckoutBeforeStr;
		productDetail.productCancellationPolicyStr = productCancellationPolicyStr;
		
		productDetail.productExtraInfoSpaceStr = productExtraInfoSpaceStr;
		productDetail.productExtraInfoGuestAccessStr = productExtraInfoGuestAccessStr;
		productDetail.productExtraInfoGuestInteractionStr = productExtraInfoGuestInteractionStr;
		productDetail.productExtraInfoNeighborhoodStr = productExtraInfoNeighborhoodStr;
		productDetail.productExtraInfoTransitStr = productExtraInfoTransitStr;
		productDetail.productExtraInfoOtherNoteStr = productExtraInfoOtherNoteStr;
		productDetail.productExtraInfoHouseRuleStr = productExtraInfoHouseRuleStr;
		productDetail.productExtraInfoHouseManualStr = productExtraInfoHouseManualStr;
		productDetail.productExtraInfoDirectionStr = productExtraInfoDirectionStr;
	    
	    var urlStrStr = pathOri + '/product/savedetail?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
	    var jsonStr = $.toJSON(productDetail);
		alert(jsonStr);
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "save product detail result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#productMoreInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function saveProductAddressDetails(pathOri)
{
	var form = $("#productAddressInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		
		var userLoginEmailStr = $("#userLoginEmailStr").html();
		var userLoginTokenStr = $("#userLoginTokenStr").html();
		
		var productAddress = {};
		var productIdStr = $("#productIdStr").html();
		var productCountryStr = $("#productCountryStr").val();
		var productStateStr = $("#productStateStr").val();
		var productCityStr = $("#productCityStr").val();
		var productZipcodeStr = $("#productZipcodeStr").val();
		var productStreetAddressStr = $("#productStreetAddressStr").val();
		var productAddressDetailStr = $("#productAddressDetailStr").val();
		
		productAddress.productIdStr = productIdStr;
		productAddress.productCountryStr = productCountryStr;
		productAddress.productStateStr = productStateStr;
		productAddress.productCityStr = productCityStr;
		productAddress.productZipcodeStr = productZipcodeStr;
		productAddress.productStreetAddressStr = productStreetAddressStr;
		productAddress.productAddressDetailStr = productAddressDetailStr;
	    
	    var urlStrStr = pathOri + '/product/updateAddress?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
	    var jsonStr = $.toJSON(productAddress);
		alert(jsonStr);
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "update product address result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#productAddressInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}

function addProductPriceMultipleOptions(pathOri)
{
	var form = $("#productPriceMultipleOptionInfo");
	form.validate();
	var ifValidateForm = form.valid();
	if (ifValidateForm) {
		
		var userLoginEmailStr = $("#userLoginEmailStr").html();
		var userLoginTokenStr = $("#userLoginTokenStr").html();
		
		var productPriceMulti = {};
		var productIdStr = $("#productIdStr").html();
		var additionalPriceKeyStr = $("#additionalPriceKeyStr").val();
		var additionalPriceValue = $("#additionalPriceValue").val();
		
		productPriceMulti.productIdStr = productIdStr;
		productPriceMulti.additionalPriceKeyStr = additionalPriceKeyStr;
		productPriceMulti.additionalPriceValue = additionalPriceValue;
	    
	    var urlStrStr = pathOri + '/product/multiprice?userLoginEmail='+userLoginEmailStr+'&userLoginToken='+userLoginTokenStr;
	    var jsonStr = $.toJSON(productPriceMulti);
		alert(jsonStr);
	    
	    $.ajax({ 
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : urlStrStr,  
	        processData : false,  
	        dataType : 'json',  
	        data : jsonStr,  
	        success : function(data) {  
	        	var dataRes = "update product price multi option result: " + data.result + "; resultDesc: " + data.resultDesc;
	            alert(dataRes);
	            var boxVar = $("#productPriceMultipleOptionInfo");
	            boxVar.append("<p>"+dataRes+"</p>");
	            
	            var divVar = $("#priceMultipleOptions");
	            divVar.append("<p>"+additionalPriceKeyStr+":"+additionalPriceValue+"</p>");
	        },  
	        error : function() {  
	            alert('Err...');  
	        }  
	    }); 
	}
}
