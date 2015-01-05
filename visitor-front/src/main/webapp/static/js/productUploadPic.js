function doProductImageUpload(pathOri) {
	var pid = $("#productIdPageTemp").html();
	var picUploadUrl = pathOri + "/product/picture?pid="+pid;
	
    $("#uploadProductPicForm").ajaxSubmit({
    	type: "post",
    	url: picUploadUrl,
    	success: function (dataT) {
    		var data = $.secureEvalJSON(dataT);
    		if (data.result == 0) {
    			checkCanPublish(data.productCan);
        		var imageUrl =  data.imageUrl;
        		var appendDiv = '<div id="pic_'+data.productPicId+'" class="imgbox"><img src="'+ imageUrl + '" width="180"/><img src="'+imgPathOriginStr+'/static/closediv.png" width="15" class="closediv" onclick="deleteProductPicture(\'this\',\''+data.productId+'\',\''+data.productPicId+'\');"/></div>';
        		$("#resultProductPicUpload").append(appendDiv);
        		$("#productPhotosLi").attr('class', 'publishchoosed');
        		$("#fileProductIcon").val("");
        		$("#remainStepsSpan").html(data.stepsCount);
    		} else {
    			alert(data.resultDesc);
    		}
        },
        error: function (msg) {
        	alert("image upload failed");
        }
    });
}

function deleteProductPicture(node, pid, picId) {
	var productDetail = {};
	
	productDetail.a = 'a';
	productDetail.b = 'b';
	
    var urlStrStr = pathGlobe + '/product/delpicture?pid=' + pid + '&picId=' + picId;
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
        	var res = checkCanPublish(data.productCan);
        	if (res == 1) {
        		$("#productPhotosLi").attr('class', 'publishchoosed');
        	} else {
        		$("#productPhotosLi").attr('class', 'choosed');
        	}
        	var idDel = "#pic_" + data.productPicId;
        	$(idDel).hide();
        },  
        error : function() {  
        }  
    });
}