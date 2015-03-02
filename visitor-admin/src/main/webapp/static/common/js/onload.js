 /**
 * 页面常用函数
 */
/*jslint browser: true*/
/*global getFormatDate, jQuery, WdatePicker, SP*/
'use strict';
//初始化时间
var be = getFormatDate(new Date().getTime() - (30 * 24 * 3600 * 1000));
var en = getFormatDate(new Date().getTime());
$("#start_time").val(be);
$("#end_time").val(en);
//additional functions for data table 自定义样式
$.fn.dataTableExt.oApi.fnPagingInfo = function (oSettings) {
    return {
        "iStart":         oSettings._iDisplayStart,
        "iEnd":           oSettings.fnDisplayEnd(),
        "iLength":        oSettings._iDisplayLength,
        "iTotal":         oSettings.fnRecordsTotal(),
        "iFilteredTotal": oSettings.fnRecordsDisplay(),
        "iPage":          Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength),
        "iTotalPages":    Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength)
    };
};
$.extend($.fn.dataTable.defaults.oLanguage, {
    "sLengthMenu" : "每页显示 _MENU_ 条记录",
    "sZeroRecords" : "没有检索到数据",
    "sEmptyTable" : "无数据",
    "sInfoEmpty" : "0条记录",
    "sSearch" : "搜索",
    "sInfo" : "当前数据从第 _START_ 到第 _END_ 条;总共 _TOTAL_ 条记录",
    "sInfoEmtpy" : "没有数据",
    "sProcessing" : "正在加载数据...",
    "sInfoFiltered" : "(从 _MAX_ 条中筛选出)",
    "oPaginate" : {
        "sFirst" : "首页",
        "sPrevious" : "前页",
        "sNext" : "后页",
        "sLast" : "尾页"
    }
});
$.extend($.fn.dataTable.defaults, {
    "bRetrieve" : true,
    "bDestroy" : false,
    "sPaginationType" : "bootstrap",
    "sServerMethod" : "POST",
    "bProcessing" : true,
    "iDisplayLength" : 15,
    "aLengthMenu" : [[5, 10, 15, 20, 30, 50, 100, -1], [5, 10, 15, 20, 30, 50, 100, "ALL"]], // change per page values here
    "bDeferRender" : true,
    "bServerSide" : true
});
jQuery('#resultTable_wrapper .dataTables_filter input').addClass("form-control input-small"); // modify table search input
jQuery('#resultTable_wrapper .dataTables_length select').addClass("form-control input-xsmall"); // modify table per page dropdown
jQuery('#resultTable_wrapper .dataTables_length select').select2(); // initialize select2 dropdown

//获取开始时间
function timeFocusBegin() {
    return WdatePicker({
        isShowClear : false,
        maxDate : '#F{$dp.$D(\'end_time\')||\'%y-%M-{%d}\'}',
        dateFmt : 'yyyy-MM-dd'
    });
}
//获取结束时间
function timeFocusEnd() {
    return WdatePicker({
        isShowClear : false,
        minDate : '#F{$dp.$D(\'start_time\')}',
        maxDate : '%y-%M-{%d}',
        dateFmt : 'yyyy-MM-dd'
    });
}

//获取一天之前时间
function timeFocusBeginOneDay() {
    WdatePicker({
        isShowClear: false,
        maxDate: '#F{$dp.$D(\'end_time\')||\'%y-%M-{%d}\'}',
        dateFmt: 'yyyy-MM-dd'
    });
}
//获取开始时间(月)
function timeMonthFocusBegin() {
    WdatePicker({
        isShowClear: false,
        maxDate: '#F{$dp.$D(\'end_time\')||\'%y-%M\'}',
        dateFmt: 'yyyy-MM'
    });
}

//获取结束时间(月)
function timeMonthFocusEnd() {
    WdatePicker({
        isShowClear: false,
        minDate: '#F{$dp.$D(\'start_time\')}',
        maxDate: '%y-%M\'}',
        dateFmt: 'yyyy-MM'
    });
}
//一级渠道和四级渠道全局变量
var oTable1 = null;
var oTable4 = null;
//一级渠道表格
function showOneModal(ctx) {
    if ($("#onesel").hasClass("disabled")) {
        return;
    }
    if (oTable1 === null) {
    	console.log();
        var options1 = {};
        options1.sAjaxSource =  ctx + "/city/getAllCity";
        options1.bServerSide = false;
        options1.bFilter = true;
        options1.aaSorting = [[1, "asc"]];
        options1.aoColumns = [{
            "sTitle" : "选择",
            "sClass" : "center",
            "mDataProp" : null,
            "sWidth" : "50px",
            "fnRender" : function (obj) {
                return '<input type="radio" name="onechannel"  value="' + obj.aData.cityId + '" />';
            },
            "bSortable" : false
        }, {
            "sTitle" : "ID",
            "sClass" : "center",
            "mDataProp" : "cityId",
            "sWidth" : "80px",
            "bSortable" : true
        }, {
            "sTitle" : "名称",
            "sClass" : "center",
            "mDataProp" : "cityName"
        }];
        oTable1 = SP.loadTableInfo($("#ochannel"), options1);
    }
    
    $("#myModal").modal();
}
//四级渠道表格
function showFourModal() {
    if ($("#foursel").hasClass("disabled")) {
        return;
    }
    var options2 = {};
    options2.sAjaxSource = "city/getAllCity";
    options2.bServerSide = false;
    options2.bFilter = true;
    options2.aoColumns = [{
        "sTitle": "选择",
        "sClass": "center",
        "mDataProp": null,
        "sWidth": "50px",
        "fnRender": function (obj) {
            return '<input type="radio" name="fychannel"  value="' + obj.aData.cityId + '" />';
        },
        "bSortable": false
    }, {
        "sTitle": "ID",
        "sClass": "center",
        "mDataProp": "cityId",
        "sWidth": "80px",
        "bSortable": true
    }, {
        "sTitle": "名称",
        "sClass": "center",
        "mDataProp": "cityName"
    }];
    //如果已经加载，则重新加载四级渠道信息
    if (oTable4 && oTable4.fnDestroy) {
        oTable4.fnDestroy();
        $("#fourchannel").empty();
        $("#fourchannel").css("width", "100%");
    }
    oTable4 = SP.loadTableInfo($("#fourchannel"), options2);
    $("#selectFourAll").click(function () {
        if (this.checked) {
            $('input[type=checkbox][name=fychannel]').attr('checked', true);
        } else {
            $('input[type=checkbox][name=fychannel]').attr('checked', false);
        }
    });
    $("#myModalFour").modal();
}
//一级渠道确定按钮事件
function sureFour() {
    var onechannel = $("input[type=checkbox][name=fychannel]:checked");
    var nowsel = [];
    /*jslint unparam: true*/
    $.each(onechannel, function (index, content) {
        nowsel.push(content.value);
    });
    /*jslint unparam: false*/
    $("#fourChannelInput").val(nowsel.join(","));
}
//四级渠道确定按钮事件
function sureOne() {
    var onechannel = $("input[type=radio][name=onechannel]:checked");
    var nowsel = [];
    /*jslint unparam: true*/
    $.each(onechannel, function (index, content) {
        nowsel.push(content.value);
    });
    /*jslint unparam: false*/
    $("#onChannelInput").val(nowsel.join(","));
}
//remove version
function removeClientVersion() {
    $("#client_version option").remove();
}
//读取AppID信息
function loadAppidInfo(os_id) {
    var defa = [{
        "os_id": os_id
    }];
    SP.loadSelectDate("base/getAppIDInfo", $("#app_id"), defa);
    removeClientVersion();
}
//读取Version信息
function loadAppidVersion(app_id) {
    var defa = [{
        "app_id": app_id
    }];
    SP.loadSelectDate("base/getAppidVersion", $("#client_version"), defa);
}
//四级渠道change事件
function fourChange(val) {
    if (val.length > 0) {
        $("#onesel").addClass("disabled");
        $("#onChannelInput").attr("disabled", true);
    } else {
        $("#onesel").removeClass("disabled");
        $("#onChannelInput").attr("disabled", false);
    }
}
//一级渠道change事件
function oneChange(val) {
    if (val.length > 0) {
        $("#foursel").addClass("disabled");
        $("#fourChannelInput").attr("disabled", true);
    } else {
        $("#foursel").removeClass("disabled");
        $("#fourChannelInput").attr("disabled", false);
    }
}
//日期格式化
function getFormatDate(date, dateformat) {
    date = new Date(date);
    if (isNaN(date)) {
        return null;
    }
    var format = dateformat || "yyyy-MM-dd";
    var o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
        "q+": Math.floor((date.getMonth() + 3) / 3),
        "S": date.getMilliseconds()
    };
    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (date.getFullYear().toString()).substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr((o[k].toString()).length));
        }
    }
    return format;
}
//时间间隔校验
function getTimeBetween(begin, end){
    if(begin && end){
        begin = begin.replace(/\-/g,'\/');
        end = end.replace(/\-/g,'\/');
        var b= parseInt(new Date(begin+" 00:00:00").getTime()/1000/60/60/24);
        var e=parseInt(new Date(end+" 00:00:00").getTime()/1000/60/60/24);
        return  parseInt(e-b);
    }
    return 0;
}
//参数校验
function checkParamters() {
    var boo = false;
    var ar = false;
    if (!($("#onChannelInput")[0].disabled) && ($("#onChannelInput").val() !== "")) {
        ar = true;
    } else if (!($("#fourChannelInput")[0].disabled) && ($("#fourChannelInput").val() !== "")) {
        ar = true;
    } else if (!($("#fromChannelInput")[0].disabled) && ($("#fromChannelInput").val() !== "")) {
        ar = true;
    }

    /* 用户属性与设备、平台、appid、版本不互斥
    if($("#appidsel").val()!="-1" || $("#device_id").val()!="-1" || $("#os_id").val()!="-1" ){
        ar=true;
    }
    if($("#client_version").val()!=null && $("#client_version").val()!="-1"){
        ar=true;
    }
    if($("#app_id").val()!=null && $("#app_id").val()!="-1"){
        ar=true;
    }
    if($("#isautos").val()!=null && $("#isautos").val()!="0"){
        ar=true;
    }*/
    if ($("#user_attr").val() !== null && $("#user_attr").val() !== "0") {
        boo = true;
    }
    if (boo && ar) {
        window.alert("暂不支持用户属性和渠道同时选择！");
        return false;
    }
    return true;
}