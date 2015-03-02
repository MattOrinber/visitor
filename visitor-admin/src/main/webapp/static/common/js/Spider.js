/*global window, jQuery, $, SP*/
(function ($) {
    //全局系统对象
    window.SP = {};
    //check函数
    SP.checker = function (form) {
        form.trigger("checkInput");
        if ($(".formErrorContent").css("display") === "block") {
            return false;
        }
        //查询时间间隔设置为三个月
        if (getTimeBetween($("#start_time").val(), $("#end_time").val()) > 90) {
            window.alert("开始时间和结束时间间隔在[1-90]范围内!");
            return;
        }
        return true;
    };
    //读取图表
    SP.loadChart = function (options) {
        var postdata;
        if (options.form) {
            postdata = SP.serializeToJson(options.form);
        } else {
            postdata = options.data || {};
        }
        if (options.chartData) {
            options.draw(options.chartId, options.name, options);
        } else {
            $.getJSON(options.url, postdata, function (data) {
                options.draw(options.chartId, options.name, {
                    chartData : data.chartData,
                    categoryField : options.categoryField,
                    valField : options.valField
                });
            });
        }
    };
    //读取表格信息
    SP.loadTableInfo = function (table, options, form) {
        var data;
        if (form) {
            data = form.serializeArray();
        } else {
            data = options.data || {};
        }
        var bServerSide;
        if (options.bServerSide === undefined) {
            bServerSide = true;
        } else {
            bServerSide = options.bServerSide;
        }
        var aaSorting = options.aaSorting || [
            [0, "desc"]
        ];
        var oTableTools = options.oTableTools || {};
        var sScrollX = options.sScrollX || "";
        var sScrollXInner = options.sScrollXInner || "";
        var bScrollCollapse = options.bScrollCollapse || false;
        return table.dataTable({
            "sAjaxSource": options.sAjaxSource,
            "aoColumns": options.aoColumns,
            //"aoColumnDefs": options.aoColumnDefs,
            "bServerSide": bServerSide,
            "aaSorting": aaSorting,
            "oTableTools": oTableTools,
            "fnServerParams": function (aoData) {
                for (var i = 0; i < data.length; i++) {
                    aoData.push(data[i]);
                }
            },
            "fnInitComplete": (options.fnInitComplete || function() {}),
            "fnRowCallback": (options.fnRowCallback || function() {}),
            "sScrollX": sScrollX,
            "sScrollXInner": sScrollXInner,
            "bScrollCollapse": bScrollCollapse
        });
    };

    SP.ajax = function(form, options, check) {
        if (check === undefined) {
            check = true;
        }
        var exec = true;
        if (check) {
            check = SP.checker(form);
            exec = check;
        }
        if (exec) {
            var p = options || {};
            var url = p.url || form.attr("action");
            var sendType = p.type || form.attr("method") || 'post';
            var pdata = p.data || form.serialize(); // SP.serializeToJson(form);
            $.ajax({
                cache: false,
                async: true,
                url: url,
                data: pdata,
                dataType: 'json',
                type: sendType,
                beforeSend: function() {
                    if (p.beforeSend)
                        p.beforeSend();
                },
                complete: function() {
                    if (p.complete)
                        p.complete();
                },
                success: function(res) {
                    if (p.success) {
                        p.success(res);
                    } else {
                        if (res.result == 'success') {
                            noty($.parseJSON('{"text":"操作成功","layout":"bottomLeft","type":"success"}'));
                        } else {
                            noty($.parseJSON('{"text":"操作失败,错误信息:' + res.result + '","layout":"bottomLeft","type":"error"}'));
                        }
                    }

                },
                error: function(res, b) {
                    if (p.error) {
                        p.error(res, b);
                    } else {
                        noty($.parseJSON('{"text":"操作失败Error,错误信息:' + res + b + '","layout":"bottomLeft","type":"error"}'));
                    }
                }
            });
        }
    };
    SP.serializeToJson = function(obj) {
        var o = {};
        var a = obj.serializeArray();
        $.each(a, function() {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
        });
        return JSON2.stringify(o);
    };
    SP.exportExcel = function(url) {
        $("#tef").attr("action", url);
        $("#tef").attr("method", "post");
        document.tef.submit();
    }

    SP.loadSelectDate = function(url, field, defaut) {
        if (field) {
            var def = "<option value='-1'>请选择</option>";
            var parm = {};
            if (defaut != null) {
                if (defaut.length == 1) {
                    parm = defaut[0] || {};
                } else if (defaut.length == 2) {
                    def = "<option value='" + defaut[0] + "'>" + defaut[1] + "</option>";
                } else if (defaut.length > 2) {
                    parm = defaut[2] || {};
                }
            }

            $.getJSON(url, parm, function(data) {
                if (data != null) {
                    field.empty();
                    field.append(def);
                    for (var i = 0; i < data.length; i++) {
                        if (null != data[i]['version']) {
                            field.append("<option value='" + data[i]['version'] + "'>" + (data[i]['version']) + "</option>");
                        } else {
                            field.append("<option value='" + data[i]['app_id'] + "'>" + (data[i]['app_name']) + "</option>");
                        }
                    }
                }
            });
        }
    };

    SP.loadVersionMultiSelect = function(url, field, defaut) {
        if (field) {
            var def = "<option value='-1'>请选择</option>";
            var parm = {};
            if (defaut != null) {
                if (defaut.length == 1) {
                    parm = defaut[0] || {};
                } else if (defaut.length == 2) {
                    def = "<option value='" + defaut[0] + "'>" + defaut[1] + "</option>";
                } else if (defaut.length > 2) {
                    parm = defaut[2] || {};
                }
            }

            $.getJSON(url, parm, function(data) {
                if (data != null && data.list != null) {
                    field.empty();
                    field.append(def);
                    for (var i = 0; i < data.list.length; i++) {
                        if (null != data.list[i]['version']) {
                            field.append("<option value='" + data.list[i]['version'] + "'>" + (data.list[i]['version']) + "</option>");
                        } else {
                            field.append("<option value='" + data.list[i]['id'] + "'>" + (data.list[i]['name'] || data.list[i]['version']) + "</option>");
                        }
                    }

                    if (field.attr("id") === "client_version") {
                        $("#client_version option[value=-1]").remove(); //remove the default select value
                        $("#client_version").attr("multiple", "multiple"); //change single select to multi select
                        field.multiselect({
                            checkAllText: "全选",
                            uncheckAllText: "取消",
                            noneSelectedText: "请选择",
                            minWidth: 160,
                            selectedList: 100, // 0-based index
                            beforeopen: function() {
                                //client_version有时为空，需要删除该option
                                $("#client_version option[value=]").remove();
                                $("#client_version").multiselect("refresh");
                            }
                        }).multiselectfilter({
                            label: '查询',
                            width: 70,
                            placeholder: ''
                        });
                        field.multiselect("refresh");
                        field.multiselect("uncheckAll");
                    }
                }
            });
        }
    };
}(jQuery));