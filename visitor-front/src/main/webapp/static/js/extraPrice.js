function addExtraPrice() {
	var variants = $.trim($("#extraVariants").val());
	var price = $.trim($("#extraPriceSet").val());
	
	if (variants != "") {
		var regExFloat = /^\d+(\.\d+)?$/;
	    var regExInt = /^[0-9]*[1-9][0-9]*$/;
	    if (regExFloat.test(price) || regExInt.test(price)) {
	    	
	    	var productIdStr = $("#productIdPageTemp").html();
	    	var productPriceExtra = {};
	    	productPriceExtra.productIdStr = productIdStr;
	    	productPriceExtra.additionalPriceKeyStr = variants;
	    	productPriceExtra.additionalPriceValue = price;
	    	
	    	var urlStrStr = pathGlobe + '/product/multiprice';
	        var jsonStr = $.toJSON(productPriceExtra);
	        
	        $.ajax({ 
	            type : 'POST',  
	            contentType : 'application/json',  
	            url : urlStrStr,  
	            processData : false,  
	            dataType : 'json',  
	            data : jsonStr,  
	            success : function(data) {  
	            	var trHTML = '<tr><td class="red">' + variants + 
	            	'</td><td class="white">$' + price + 
	            	'</td><td align="center"><img src="' + pathGlobe + 
	            	'/static/img/close1.png" width="13" onclick="removeExtraPrice(this);" data-pmp-id="'+ data.pmpId +'"/></td></tr>';
	            	
	    			$("#productExtraPriceTab").append(trHTML);
	            },  
	            error : function() {  
	                alert('Err...');  
	            }  
	        });
		} else {
			$("#extraPriceSet").val("");
			alert("please set a right price format");
		}
	}
}

function removeExtraPrice(node) {
	var productIdStr = $("#productIdPageTemp").html();
	var pmpIdStr = $(node).attr("data-pmp-id");
	var keyStr = $(node).parent().parent().find('[class="white"]').html();
	var pmp = {};
	pmp.productIdStr = productIdStr;
	pmp.pmpIdStr = pmpIdStr;
	pmp.additionalPriceKeyStr = keyStr;
	
	var urlStrStr = pathGlobe + '/product/delmultiprice';
    var jsonStr = $.toJSON(pmp);
    
    $.ajax({ 
        type : 'POST',  
        contentType : 'application/json',  
        url : urlStrStr,  
        processData : false,  
        dataType : 'json',  
        data : jsonStr,  
        success : function(data) {  
        	$(node).parent().parent().remove();
        },  
        error : function() {  
            alert('Err...');  
        }  
    });
}
