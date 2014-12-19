function doProductImageUpload(pathOri) {
	$("#uploadProductPicForm").submit(function() {
    	
		var pid = $("#productIdPageTemp").html();
    	var picUploadUrl = pathOri + "/product/picture?pid="+pid;
    	
        $("#uploadProductPicForm").ajaxSubmit({
        	type: "post",
        	url: picUploadUrl,
        	success: function (data) {
        		if (data.result == 0) {
	        		var imageUrl =  data.imageUrl;
	        		$("#resultProductPicUpload").append("<span>" + imageUrl + "</span><br />");
        		} else {
        			alert(data.resultDesc);
        		}
            },
            error: function (msg) {
            	alert("image upload failed");
            }
        });
        return false;
    });
    $("#uploadProductPicForm").submit();
}