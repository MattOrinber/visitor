		/*删除一行记录*/
		function deleteItem(rowId){
			if(!confirm('您真的要删除这一行吗？')){
				return;
			}
			
			/*1.将其后面的每行的排序值减1*/
			var aftCurrentRow = $('#tbitem tbody tr:gt('+rowId+')');
			aftCurrentRow.each(function(){
				var node = $(this).children("td:first");
				node.text(Number(node.text())-1);
			});
			
			/*移除本行*/
			$('#tbitem tbody tr:eq('+rowId+')').remove();
		}
		
		/*显示编辑框*/
		function showEditDiv(rowId){
			//alert("called");
			var $doc = $(document);
			var $form = $('#paramDiv');
			
			/*更新数据*/
			var currentRow = $('#tbitem tbody tr:eq('+rowId+')');
			var tmpName = currentRow.children("td:eq(2)").text();
			var paramContent = currentRow.children("td:eq(4)").text();
			
			$('#tmpName').text(tmpName);
			$('#paramContent2').text(paramContent);
			
			
			$('#editParamDiv').show();
			var top = Math.max((($doc.outerHeight() - $form.outerHeight()) / 2)-100, 0);
			var left = Math.max((($doc.outerWidth() - $form.outerWidth()) / 2), 0);
			
			$form.offset({top: top, left: left});
			
			$('#mask2').css('height', $doc.outerHeight());
		}

		$(document).ready(function(){
			
			var showAddNewDiv = function(){
				var $doc = $(document);
				var $form = $('#newItemDiv');
				
				$('#addNewDiv').show();
				var top = Math.max((($doc.outerHeight() - $form.outerHeight()) / 2)-100, 0);
				var left = Math.max((($doc.outerWidth() - $form.outerWidth()) / 2), 0);
				
				$form.offset({top: top, left: left});
				
				$('#mask1').css('height', $doc.outerHeight());
			};
			
			var initList = function(index){
				
				$('#tmpList')[0].options.length = 0;
				$('#tmpList')[0].options.add(new Option('--请选择--','0'));
				
				var items = tempInfo[index].item;
				var item_len = items.length;
				for(var i=0;i &lt; item_len;i++){
					$('#tmpList')[0].options.add(new Option(items[i].tmpName,items[i].tmpId));
				}
				
				$('#tmpList')[0].selectedIndex = 0;
			};	
			
			/*初始化位置下拉框*/
			var initPosList = function(){
				$('#tmpPos')[0].options.length = 0;
				$('#tmpPos')[0].options.add(new Option("添加到最前面",0));
				
				$("#tbitem tbody tr").each(function(){
					var sortId = $(this).children("td:first").text();
					var text = $(this).children("td:eq(2)").text();
					
					$('#tmpPos')[0].options.add(new Option("添加到第"+sortId+"行:'"+text+"'之后",sortId));
				});
				
				$('#tmpPos')[0].selectedIndex = 0;
			};
			
			var validCheck = function(){
				return true;
			};
			
			/*add a new row*/
			var addNewRow = function(){
				var tmpPos = $('#tmpPos').val();/*位置*/
				var tdIndex = (tmpPos -1)>0 ? (tmpPos-1) : 0;
				var aftCurrentRow = $('#tbitem tbody tr:gt('+tdIndex+')');
				/*这里遇到一个问题，使用ge，程序出错，gte的话呢，
				 * 后续的循环又不执行，所以暂时还是用gt，这样就不包括当前行了
				*/
				/*将后续的每一行的位置值加1*/
				aftCurrentRow.each(function(){
					var node = $(this).children("td:first");
					node.text(Number(node.text())+1);
				});
				
				var currentRow = $('#tbitem tbody tr:eq('+tdIndex+')');
				/*显示渲染，不知道为什么*/
				var btnCss ="button button-delete ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-primary";
				var htmDeleteBtn = "&lt;span class='ui-button-icon-primary ui-icon ui-icon-trash'&gt;&lt;/span&gt;&lt;span class='ui-button-text'&gt;删除&lt;/span&gt;";
				var htmEditBtn = "&lt;span class='ui-button-icon-primary ui-icon ui-icon-pencil'&gt;&lt;/span&gt;&lt;span class='ui-button-text'&gt;修改参数&lt;/span&gt;";
								
				var newRow="&lt;tr&gt;";
				/*第一列,位置序号*/
				newRow+="&lt;td&gt;"+(Number(tmpPos)+1)+"&lt;/td&gt;";
				/*第二列*/
				newRow+="&lt;td&gt;"+$('#tmpList').val()+"&lt;/td&gt;";
				/*第三列*/
				newRow+="&lt;td&gt;&lt;a href='${pageContext.request.contextPath}/domain/template/show/"+$('#tmpList').val()+"'&gt;"+$('#tmpList').find("option:selected").text()+"&lt;/a&gt;&lt;/td&gt;";
				/*第四列*/
				newRow+="&lt;td&gt;"+$('input[name=tmpStyle]:checked').next().text()+"&lt;/td&gt;";
				/*第五列，参数*/
				newRow+="&lt;td&gt;"+$('#paramContent').text()+"&lt;/td&gt;";
				/*第六列，操作选项*/
				newRow+="&lt;td&gt;&lt;a href='#' class='"+btnCss+"'&gt;"+htmDeleteBtn+"&lt;/a&gt;";
				if($('input[name=tmpStyle]:checked').val()=="2"){
					/*如果是Tiles组件，需要编辑参数*/
					newRow+="&lt;a href='#' class='"+btnCss+"'&gt;"+htmEditBtn+"&lt;/a&gt;";
				}
				
				newRow+="&lt;/td&gt;&lt;/tr&gt;";
				//alert($('input[name=tmpStyle]:checked').val());
				//add a new line
				if(tmpPos > 0){
					currentRow.after(newRow);//insert into the proper position
				}else{
					currentRow.before(newRow);//insert the row as the first row
					/*只有在这种情况下，当前行值才需要更新*/
					var currNode = $(currentRow).children("td:first");
					currNode.text(Number(currNode.text())+1);
				}
			};
			
			$('#addNew').click(function(){
				showAddNewDiv();
				initPosList();
			});
		
			$('#btnCancel').on('click', function(){
				$('#addNewDiv').hide();
			});
			
			/*关闭修改参数的窗口*/
			$('#btnParamCancel').on('click', function(){
				$('#editParamDiv').hide();
			});
			
			$('#btnOk').on('click',function(){
				/*有效性检查*/
				if(validCheck()==true){
					addNewRow();
					$('#addNewDiv').hide();
				}
			});
			
			
			$('input[name=tmpStyle]').change(function () {
	            var lstIndex = $(this).val();
	            initList(lstIndex);
	            
	            if(lstIndex==2){
	    		    $('#noparam').hide();
	    		    $('#showparam').show();
	            }else{
	    		    $('#noparam').show();
	    		    $('#showparam').hide();
	            }
	            
	        });
			
			/*初始化*/
		    var tempInfo = eval('${tempJson}');
		    initList(0);	
		    
		    $('#noparam').show();
		    $('#showparam').hide();
		});
