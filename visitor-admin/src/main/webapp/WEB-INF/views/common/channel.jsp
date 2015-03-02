<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="form-group" style="width: 350px">
	<label for="appendedInputButton">一级渠道：</label>
	<input style="width: 200px;"
		onchange="oneChange(this.value)" name="templateVo.one_channel_id"
		class="form-control validate[custom[number]]"
		id="onChannelInput" size="16" type="text" value="">
	<button onclick="showOneModal()" id="onesel" class="btn btn-default" type="button">选择</button>
</div>
<div class="form-group" style="width: 350px">
	<label for="appendedInputButton">四级渠道：</label>
	<input style="width: 200px;"
		onchange="fourChange(this.value)"
		name="templateVo.four_channel_fromid"
		class="form-control validate[custom[number]]"
		id="fourChannelInput" size="16" type="text" value="">
	<button onclick="showFourModal()" id="foursel" class="btn btn-default" type="button">选择</button>
</div>

<!-- One From Modal -->
<div id="myModal" class="modal fade" tabindex="-1" role="basic" aria-hidden="true" style="display: none; ">
	<div class="modal-dialog modal-wide">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="myModalLabel">选择一级渠道</h3>
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

<!-- Four From Modal -->
<div id="myModalFour" class="modal fade" tabindex="-1" role="basic" aria-hidden="true" style="display: none; ">
	<div class="modal-dialog modal-wide">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="myModalLabel">选择四级渠道</h3>
			</div>
			<div class="modal-body" >
				<div class="scroller" style="height:350px" data-always-visible="1" data-rail-visible1="1">
					<table id="fourchannel"
						class="table table-striped table-bordered table-condensed bootstrap-datatable datatable">
						<thead>
						</thead>
					</table>
				</div>
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" onclick="sureFour()"	class="btn btn-primary">确定</a> 
				<a class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</a>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>