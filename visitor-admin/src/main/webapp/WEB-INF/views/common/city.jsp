<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="form-group">
    <label for="name" class="col-sm-2 control-label">位置对应城市:</label>
    <div class="col-sm-10">
        <input type="text" id="onChannelInput" name="containerProductkey"  value="${container.containerProductkey}" class="form-control "/>
   		<button onclick="showOneModal('${ctx}')" id="onesel" class="btn btn-default " type="button">选择城市</button>
    </div>
</div>

<!-- One From Modal -->
<div id="myModal" class="modal fade" tabindex="-1" role="basic" aria-hidden="true" style="display: none; ">
	<div class="modal-dialog modal-wide">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="myModalLabel">选择城市</h3>
			</div>
			<div class="modal-body" >
				<div class="scroller" style="height:350px" data-always-visible="1" data-rail-visible1="1">
					<table id="ochannel"
						class="table table-striped table-bordered table-condensed bootstrap-datatable datatable">
						<thead>
						</thead>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" onclick="sureOne()"	class="btn btn-primary">确定</a> 
				<a class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</a>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
