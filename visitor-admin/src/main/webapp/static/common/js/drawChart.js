/**
 * 绘制图表
 */
'use strict';
/*global Highcharts*/
var DrawChart = (function () {
    var config = {
        //标题
        title : {
            text : '对比图'
        },

        //日期范围
        rangeSelector: {
            buttons: [{
                count: 3,
                type: 'minute',
                text: '3分'
            }, {
                count: 5,
                type: 'minute',
                text: '5分'
            }, {
                type: 'all',
                text: '全部'
            }],
            inputEnabled: false,
            selected: 0
        },
        //导出
        exporting: {
            enabled: true
        },
        //图例
        legend: {
            layout: 'horizontal',
            align: 'center',
            verticalAlign: 'bottom',
            borderWidth: 0,
            enabled : true
        },
        //loading
        loading: {
            hideDuration: 5000,
            showDuration: 5000,
        },
        //提示
        tooltip: {
            shared: true, //是否共享提示，也就是X一样的所有点都显示出来
            useHTML: true, //是否使用HTML编辑提示信息
            xDateFormat: '%Y-%m-%d %H:%M:%S',//日期格式
            headerFormat: '<small>{point.key}</small><table>',
            pointFormat: '<tr><td style="color: {series.color}">{series.name}: </td>' + '<td style="text-align: right"><b>{point.y}</b></td></tr>',
            footerFormat: '</table>',
            valueDecimals: 0 //数据值保留小数位数
        }
    };
    //init highchart option
    var initHighChartsOption = function () {
        Highcharts.setOptions({
            global : {
                useUTC : false
            },
            lang : {
                loading: '数据加载中...'
            }
        });
    };
    //set chart option title
    var setConfigTitle = function (title) {
        config.title.text = title;
    };
    var initHighChartsTitleOption = function (title) {
        setConfigTitle(title);
    };
    return {
        init : function (divid, title, data, options) {
            initHighChartsOption();
            initHighChartsTitleOption(title);
            //chart data sort by chart category field 
            data.chartData.sort(function (a, b) {
                return a[data.categoryField] > b[data.categoryField] ? 1 : -1;
            });
            var chart = new Highcharts.Chart({
                chart : {
                    renderTo : divid,
                    type : 'line'
                },
                title : config.title || options.title,
                exporting: config.exporting || options.exporting,
                legend: config.legend || options.legend,
                loading: config.loading || options.loading,
                tooltip: config.tooltip || options.tooltip,
                xAxis: {
                    type: 'datetime',
                    labels: {
                        rotation: -30,
                        y : 30
                    },
                    startOnTick: 'yes',
                    endOnTick: 'yes',
                    minorTickInterval: 'auto',//设置是否出现纵向小标尺  
                    minorGridLineColor:'#E0E0E0',  
                    lineColor: '#197F07',//设置X轴颜色  
                    tickWidth: 1,//设置X轴坐标点是否出现占位及其宽度  
                    tickPixelInterval: 40,//设置横坐标密度  
                    gridLineColor: '#E0E0E0',//设置纵向标尺颜色  
                    gridLineWidth: 1,//设置纵向标尺宽度 
                    minTickInterval : 1,
                    maxTickInterval : 10,
                    tickInterval : (function () {
                        parseInt(data.chartData.length / 30, 10) + 1;
                    }()),
                    categories : (function () {
                        var categoriesArray = [];
                        $.each(data.chartData, function (index, item) {
                            categoriesArray.push(item[data.categoryField]);
                        });
                        return categoriesArray;
                    }())
                },
                yAxis: {
                    title: {
                        text: '数量'
                    },
                    min: 0,
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                series : (function () {
                    var seriesArray = [];
                    var dataArray = [];
                    $.each(data.valField, function(index, val) {
                        var valArray = val.split(",");
                        var dataArray = [];
                        //build series data array 
                        $.each(data.chartData, function (index, item) {
                            dataArray.push(parseFloat(item[valArray[0]]));
                        });
                        //build series series array
                        seriesArray.push({
                            "name" : valArray[1],
                            "data" : dataArray
                        });
                    });
                    return seriesArray;
                }())
            });
            return chart;
        }
    };
}());